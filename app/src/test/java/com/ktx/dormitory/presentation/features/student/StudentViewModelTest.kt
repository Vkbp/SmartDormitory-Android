package com.ktx.dormitory.presentation.features.student

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.ktx.dormitory.data.repository.MockDataProvider
import com.ktx.dormitory.domain.repository.UserRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class StudentViewModelTest {

    private lateinit var viewModel: StudentViewModel
    private val userRepository = mockk<UserRepository>()
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        
        // Mock default success behavior for init { loadAllData() }
        coEvery { userRepository.getProfile() } returns Result.success(MockDataProvider.getMockProfile())
        coEvery { userRepository.getRoomInfo() } returns Result.success(MockDataProvider.getMockRoomInfo())
        coEvery { userRepository.getApplicationTimeline() } returns Result.success(MockDataProvider.getMockApplication())
        coEvery { userRepository.getPaymentHistory() } returns Result.success(MockDataProvider.getMockTransactions())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadAllData success updates state with all data`() = runTest {
        // GIVEN: Repository methods have a tiny delay to allow capture of loading state
        coEvery { userRepository.getProfile() } coAnswers {
            delay(1)
            Result.success(MockDataProvider.getMockProfile())
        }

        // WHEN: Instantiate ViewModel
        viewModel = StudentViewModel(userRepository)

        // THEN: Observe states
        viewModel.uiState.test {
            // 1. Loading state (Triggered by init)
            val loadingItem = awaitItem()
            assertThat(loadingItem.isLoading).isTrue()
            
            // 2. Success state
            val successState = awaitItem()
            assertThat(successState.isLoading).isFalse()
            assertThat(successState.profile).isEqualTo(MockDataProvider.getMockProfile())
            assertThat(successState.roomInfo).isEqualTo(MockDataProvider.getMockRoomInfo())
            
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `loadAllData partial success when room info fails`() = runTest {
        coEvery { userRepository.getProfile() } coAnswers { delay(1); Result.success(MockDataProvider.getMockProfile()) }
        coEvery { userRepository.getRoomInfo() } coAnswers { delay(1); Result.failure(Exception("Room Error")) }
        
        viewModel = StudentViewModel(userRepository)

        viewModel.uiState.test {
            awaitItem() // Loading
            
            val state = awaitItem()
            assertThat(state.profile).isNotNull()
            assertThat(state.roomInfo).isNull()
            assertThat(state.error).isNull() 
            
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `loadAllData failure when profile fails sets error`() = runTest {
        coEvery { userRepository.getProfile() } coAnswers { delay(1); Result.failure(Exception("Profile Error")) }
        
        viewModel = StudentViewModel(userRepository)

        viewModel.uiState.test {
            awaitItem() // Loading
            
            val state = awaitItem()
            assertThat(state.profile).isNull()
            assertThat(state.error).isEqualTo("Không thể tải dữ liệu")
            
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `updateProfile success updates local state`() = runTest {
        // Initialize
        viewModel = StudentViewModel(userRepository)
        
        val newName = "Nguyễn Văn B"
        val newPhone = "0987654321"
        val newEmail = "vanb@example.com"
        
        coEvery { userRepository.updateProfile(any()) } coAnswers { delay(1); Result.success(Unit) }

        viewModel.uiState.test {
            // Initial state (after loadAllData success)
            // Skip loading and final state of init load
            val initial = awaitItem()
            if (initial.isLoading) awaitItem()
            
            viewModel.updateProfile(newName, newPhone, newEmail, onSuccess = {})

            // 1. Loading
            assertThat(awaitItem().isLoading).isTrue()
            
            // 2. Updated
            val updatedState = awaitItem()
            assertThat(updatedState.isLoading).isFalse()
            assertThat(updatedState.profile?.fullName).isEqualTo(newName)
            
            cancelAndIgnoreRemainingEvents()
        }
    }
}
