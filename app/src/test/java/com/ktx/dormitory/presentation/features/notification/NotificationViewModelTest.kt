package com.ktx.dormitory.presentation.features.notification

import androidx.lifecycle.SavedStateHandle
import com.google.common.truth.Truth.assertThat
import com.ktx.dormitory.domain.model.Notification
import com.ktx.dormitory.domain.repository.NotificationRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NotificationViewModelTest {

    private lateinit var viewModel: NotificationViewModel
    private val repository = mockk<NotificationRepository>()
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        coEvery { repository.getNotifications() } returns Result.success(emptyList())
        viewModel = NotificationViewModel(repository, SavedStateHandle())
        testDispatcher.scheduler.advanceUntilIdle()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadNotifications success updates state`() = runTest(testDispatcher) {
        val notifications = listOf(
            Notification("1", "Title", "Message", "10:00", false)
        )
        coEvery { repository.getNotifications() } returns Result.success(notifications)

        viewModel.loadNotifications()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertThat(state.isLoading).isFalse()
        assertThat(state.notifications).hasSize(1)
        assertThat(state.notifications[0].title).isEqualTo("Title")
    }

    @Test
    fun `loadNotifications failure updates error state`() = runTest(testDispatcher) {
        coEvery { repository.getNotifications() } returns Result.failure(Exception("Error"))

        viewModel.loadNotifications()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertThat(state.isLoading).isFalse()
        assertThat(state.error).isEqualTo("Error")
    }

    @Test
    fun `markAsRead updates state locally`() = runTest(testDispatcher) {
        val notifications = listOf(
            Notification("1", "Title", "Message", "10:00", false)
        )
        coEvery { repository.getNotifications() } returns Result.success(notifications)
        coEvery { repository.markAsRead("1") } returns Result.success(Unit)

        viewModel.loadNotifications()
        advanceUntilIdle()
        
        viewModel.markAsRead("1")
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertThat(state.notifications[0].isRead).isTrue()
    }
}
