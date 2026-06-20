package com.ktx.dormitory.presentation.features.request

import androidx.lifecycle.SavedStateHandle
import com.google.common.truth.Truth.assertThat
import com.ktx.dormitory.domain.model.DormRequest
import com.ktx.dormitory.domain.model.RequestStatus
import com.ktx.dormitory.domain.model.RequestType
import com.ktx.dormitory.domain.repository.RequestRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RequestViewModelTest {

    private lateinit var viewModel: RequestViewModel
    private val repository = mockk<RequestRepository>()
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        coEvery { repository.getMyRequests() } returns Result.success(emptyList())
        viewModel = RequestViewModel(repository, SavedStateHandle())
        testDispatcher.scheduler.advanceUntilIdle()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadMyRequests success updates uiState`() = runTest(testDispatcher) {
        val requests = listOf(
            DormRequest("1", "A", "S1", RequestType.REPAIR, "C", RequestStatus.PENDING, "T")
        )
        coEvery { repository.getMyRequests() } returns Result.success(requests)

        viewModel.loadMyRequests()
        advanceUntilIdle()

        assertThat(viewModel.uiState.value.requests).hasSize(1)
        assertThat(viewModel.uiState.value.isLoading).isFalse()
    }

    @Test
    fun `submitRequest success resets form and reloads list`() = runTest(testDispatcher) {
        coEvery { repository.submitRequest(any(), any()) } returns Result.success(Unit)
        coEvery { repository.getMyRequests() } returns Result.success(emptyList())

        viewModel.onContentChange("New Content")
        viewModel.submitRequest()
        advanceUntilIdle()

        assertThat(viewModel.uiState.value.submitSuccess).isTrue()
        assertThat(viewModel.formState.value.content).isEmpty()
    }
}
