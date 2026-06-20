package com.ktx.dormitory.presentation.features.auth

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ktx.dormitory.domain.usecase.auth.*
import com.ktx.dormitory.domain.model.UserData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val getAuthStateUseCase: GetAuthStateUseCase,
    private val changePasswordUseCase: ChangePasswordUseCase,
    private val forgotPasswordUseCase: ForgotPasswordUseCase,
    private val resetPasswordUseCase: ResetPasswordUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    // Sử dụng SavedStateHandle để bảo vệ state khỏi Process Death
    open val uiState: StateFlow<LoginUiState> = savedStateHandle.getStateFlow("uiState", LoginUiState())

    private fun updateUiState(reducer: (LoginUiState) -> LoginUiState) {
        savedStateHandle["uiState"] = reducer(uiState.value)
    }

    init {
        if (uiState.value.userData == null) {
            fetchCurrentUser()
        }
    }

    open fun fetchCurrentUser() {
        viewModelScope.launch {
            getAuthStateUseCase().onSuccess { data ->
                updateUiState { it.copy(userData = data) }
            }
        }
    }

    open fun performLogin(
        usernameOrEmail: String,
        password: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        updateUiState { it.copy(mssvError = null, passwordError = null, error = null) }

        if (usernameOrEmail.isBlank()) {
            updateUiState { it.copy(mssvError = "MSSV hoặc Email không được để trống") }
            return
        }
        if (password.isBlank()) {
            updateUiState { it.copy(passwordError = "Mật khẩu không được để trống") }
            return
        }

        viewModelScope.launch {
            updateUiState { it.copy(isLoading = true) }
            loginUseCase(usernameOrEmail, password)
                .onSuccess { data ->
                    updateUiState { it.copy(isLoading = false, userData = data) }
                    onSuccess(data.role ?: "STUDENT")
                }
                .onFailure { e ->
                    val errorMsg = e.message ?: "Đăng nhập thất bại"
                    updateUiState { it.copy(isLoading = false, error = errorMsg) }
                    onError(errorMsg)
                }
        }
    }

    open fun logout(onComplete: () -> Unit) {
        viewModelScope.launch {
            logoutUseCase()
            updateUiState { it.copy(userData = null) }
            onComplete()
        }
    }

    open fun checkAuthStatus(onResult: (String?) -> Unit) {
        viewModelScope.launch {
            getAuthStateUseCase().onSuccess { data ->
                updateUiState { it.copy(userData = data) }
                onResult(data.role)
            }.onFailure { onResult(null) }
        }
    }

    open fun loginWithBiometric(onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            updateUiState { it.copy(isLoading = true) }
            getAuthStateUseCase().onSuccess { data ->
                updateUiState { it.copy(isLoading = false, userData = data) }
                onSuccess(data.role ?: "STUDENT")
            }.onFailure {
                updateUiState { it.copy(isLoading = false) }
                onError("Phiên đăng nhập hết hạn")
            }
        }
    }

    open fun changePassword(oldPass: String, newPass: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            updateUiState { it.copy(isLoading = true) }
            changePasswordUseCase(oldPass, newPass).onSuccess {
                updateUiState { it.copy(isLoading = false) }
                onSuccess()
            }.onFailure {
                updateUiState { it.copy(isLoading = false) }
                onError(it.message ?: "Lỗi")
            }
        }
    }

    open fun forgotPassword(email: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            updateUiState { it.copy(isLoading = true) }
            forgotPasswordUseCase(email).onSuccess {
                updateUiState { it.copy(isLoading = false) }
                onSuccess()
            }.onFailure {
                updateUiState { it.copy(isLoading = false) }
                onError(it.message ?: "Lỗi")
            }
        }
    }

    open fun resetPassword(token: String, newPass: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            updateUiState { it.copy(isLoading = true) }
            resetPasswordUseCase(token, newPass).onSuccess {
                updateUiState { it.copy(isLoading = false) }
                onSuccess()
            }.onFailure {
                updateUiState { it.copy(isLoading = false) }
                onError(it.message ?: "Lỗi đặt lại mật khẩu")
            }
        }
    }

    open fun getUserInfo(onSuccess: (UserData) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            getAuthStateUseCase().onSuccess {
                updateUiState { it.copy(userData = it.userData) }
                onSuccess(it)
            }.onFailure {
                onError(it.message ?: "Lỗi tải thông tin")
            }
        }
    }
}
