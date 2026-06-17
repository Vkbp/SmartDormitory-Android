package com.ktx.dormitory.presentation.features.auth

import com.google.common.truth.Truth.assertThat
import com.ktx.dormitory.domain.model.LoginResponse
import com.ktx.dormitory.domain.model.UserData
import com.ktx.dormitory.domain.repository.AuthRepository
import com.ktx.dormitory.domain.repository.UserRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    private lateinit var viewModel: LoginViewModel
    private val authRepository = mockk<AuthRepository>()
    private val userRepository = mockk<UserRepository>(relaxed = true)
    
    // Sử dụng StandardTestDispatcher để kiểm soát chính xác execution flow
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        coEvery { authRepository.getCurrentUser() } returns Result.failure(Exception("None"))
        viewModel = LoginViewModel(authRepository, userRepository)
        testDispatcher.scheduler.advanceUntilIdle() // init block xong
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `performLogin success updates state and triggers navigation`() = runTest(testDispatcher) {
        val username = "user01"
        val role = "USER"
        
        coEvery { authRepository.login(any(), any()) } returns Result.success(
            LoginResponse("access", "refresh", role)
        )
        coEvery { authRepository.getCurrentUser() } returns Result.success(
            UserData(username, role, "Nguyen Van A")
        )

        viewModel.performLogin(username, "password123", onSuccess = {}, onError = {})
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertThat(state.isLoading).isFalse()
        assertThat(state.error).isNull()
        assertThat(state.userData?.role).isEqualTo(role)
        assertThat(state.userData?.fullName).isEqualTo("Nguyen Van A")
    }

    @Test
    fun `performLogin failure updates state with error message`() = runTest(testDispatcher) {
        val errorMsg = "Lỗi đăng nhập"
        coEvery { authRepository.login(any(), any()) } returns Result.failure(Exception(errorMsg))

        viewModel.performLogin("wrong", "pass", onSuccess = {}, onError = {})
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertThat(state.isLoading).isFalse()
        assertThat(state.error).isEqualTo(errorMsg)
    }

    @Test
    fun `performLogin with empty fields sets errors`() = runTest(testDispatcher) {
        viewModel.performLogin("", "", onSuccess = {}, onError = {})
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertThat(state.mssvError).isNotEmpty()
        assertThat(state.passwordError).isNotEmpty()
    }
}
