package com.ktx.dormitory.presentation.features.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ktx.dormitory.domain.repository.UserRepository
import com.ktx.dormitory.domain.model.UserData
import com.ktx.dormitory.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginUiState(
    val isLoading: Boolean = false,
    val mssvError: String? = null,
    val passwordError: String? = null,
    val userData: UserData? = null,
    val error: String? = null
)

@HiltViewModel
open class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    // Thêm open để MockK có thể override trên Android
    var isLoading by mutableStateOf(false)
    var mssvError by mutableStateOf<String?>(null)
    var passwordError by mutableStateOf<String?>(null)

    private val _userData = MutableStateFlow<UserData?>(null)
    open val userData: StateFlow<UserData?> = _userData.asStateFlow()

    private val _uiState = MutableStateFlow(LoginUiState())
    // Thuộc tính uiState PHẢI open để bài UI Test không bị crash
    open val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    init {
        fetchCurrentUser()
    }

    private fun updateUserData(data: UserData?) {
        _userData.value = data
        _uiState.update { it.copy(userData = data) }
    }

    private fun updateLoading(loading: Boolean) {
        this.isLoading = loading
        _uiState.update { it.copy(isLoading = loading) }
    }

    open fun fetchCurrentUser() {
        viewModelScope.launch {
            authRepository.getCurrentUser().onSuccess { data ->
                updateUserData(data)
            }
        }
    }

    open fun performLogin(
        usernameOrEmail: String,
        password: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        mssvError = null
        passwordError = null
        _uiState.update { it.copy(mssvError = null, passwordError = null, error = null) }

        var isValid = true
        if (usernameOrEmail.isBlank()) {
            mssvError = "MSSV hoặc Email không được để trống"
            _uiState.update { it.copy(mssvError = "MSSV hoặc Email không được để trống") }
            isValid = false
        }
        if (password.isBlank()) {
            passwordError = "Mật khẩu không được để trống"
            _uiState.update { it.copy(passwordError = "Mật khẩu không được để trống") }
            isValid = false
        }

        if (!isValid) return

        viewModelScope.launch {
            updateLoading(true)
            val result = authRepository.login(usernameOrEmail, password)
            
            if (result.isSuccess) {
                val data = result.getOrNull()!!
                userRepository.saveLoginStatus(true)
                val user = UserData(usernameOrEmail, data.role, "")
                _userData.value = user
                _uiState.update { it.copy(isLoading = false, userData = user) }
                this@LoginViewModel.isLoading = false
                fetchCurrentUser()
                onSuccess(data.role)
            } else {
                val errorMsg = result.exceptionOrNull()?.message ?: "Đăng nhập thất bại"
                _uiState.update { it.copy(isLoading = false, error = errorMsg) }
                this@LoginViewModel.isLoading = false
                onError(errorMsg)
            }
        }
    }

    open fun logout(onComplete: () -> Unit) {
        viewModelScope.launch {
            authRepository.logout()
            userRepository.saveLoginStatus(false)
            updateUserData(null)
            onComplete()
        }
    }

    open fun checkAuthStatus(onResult: (String?) -> Unit) {
        viewModelScope.launch {
            authRepository.getCurrentUser().onSuccess { data ->
                updateUserData(data)
                onResult(data.role)
            }.onFailure { onResult(null) }
        }
    }

    open fun loginWithBiometric(onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            updateLoading(true)
            authRepository.getCurrentUser().onSuccess { data ->
                updateUserData(data)
                updateLoading(false)
                onSuccess(data.role)
            }.onFailure {
                updateLoading(false)
                onError("Phiên đăng nhập hết hạn")
            }
        }
    }

    open fun changePassword(oldPass: String, newPass: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            updateLoading(true)
            authRepository.changePassword(oldPass, newPass).onSuccess {
                updateLoading(false)
                onSuccess()
            }.onFailure {
                updateLoading(false)
                onError(it.message ?: "Lỗi")
            }
        }
    }

    open fun forgotPassword(email: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            updateLoading(true)
            authRepository.forgotPassword(email).onSuccess {
                updateLoading(false)
                onSuccess()
            }.onFailure {
                updateLoading(false)
                onError(it.message ?: "Lỗi")
            }
        }
    }

    open fun resetPassword(token: String, newPass: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            updateLoading(true)
            authRepository.resetPassword(token, newPass).onSuccess {
                updateLoading(false)
                onSuccess()
            }.onFailure {
                updateLoading(false)
                onError(it.message ?: "Lỗi đặt lại mật khẩu")
            }
        }
    }

    open fun getUserInfo(onSuccess: (UserData) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            authRepository.getCurrentUser().onSuccess {
                updateUserData(it)
                onSuccess(it)
            }.onFailure {
                onError(it.message ?: "Lỗi tải thông tin")
            }
        }
    }
}
