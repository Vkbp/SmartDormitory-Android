package com.ktx.dormitory.presentation.features.auth

import androidx.lifecycle.SavedStateHandle
import com.google.common.truth.Truth.assertThat
import com.ktx.dormitory.domain.model.UserData
import com.ktx.dormitory.domain.usecase.auth.*
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
    private val loginUseCase = mockk<LoginUseCase>()
    private val logoutUseCase = mockk<LogoutUseCase>()
    private val getAuthStateUseCase = mockk<GetAuthStateUseCase>()
    private val changePasswordUseCase = mockk<ChangePasswordUseCase>()
    private val forgotPasswordUseCase = mockk<ForgotPasswordUseCase>()
    private val resetPasswordUseCase = mockk<ResetPasswordUseCase>()
    
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        coEvery { getAuthStateUseCase() } returns Result.failure(Exception("None"))
        viewModel = LoginViewModel(
            loginUseCase, logoutUseCase, getAuthStateUseCase,
            changePasswordUseCase, forgotPasswordUseCase, resetPasswordUseCase,
            SavedStateHandle()
        )
        testDispatcher.scheduler.advanceUntilIdle()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `performLogin success updates state`() = runTest(testDispatcher) {
        val username = "user01"
        val role = "STUDENT"
        val userData = UserData(username, role, "Nguyen Van A")
        
        coEvery { loginUseCase(any(), any()) } returns Result.success(userData)

        viewModel.performLogin(username, "password123", onSuccess = {}, onError = {})
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertThat(state.isLoading).isFalse()
        assertThat(state.userData?.fullName).isEqualTo("Nguyen Van A")
    }
}
