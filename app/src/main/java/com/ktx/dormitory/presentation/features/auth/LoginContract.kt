package com.ktx.dormitory.presentation.features.auth

import android.os.Parcelable
import com.ktx.dormitory.domain.model.UserData
import kotlinx.parcelize.Parcelize

@Parcelize
data class LoginUiState(
    val isLoading: Boolean = false,
    val userData: UserData? = null,
    val mssvError: String? = null,
    val passwordError: String? = null,
    val error: String? = null
) : Parcelable

sealed class LoginUiEvent {
    data class LoginClicked(val mssv: String, val pass: String) : LoginUiEvent()
    object BiometricClicked : LoginUiEvent()
    object LogoutClicked : LoginUiEvent()
    data class ForgotPasswordClicked(val email: String) : LoginUiEvent()
}

sealed class LoginUiEffect {
    data class NavigateToHome(val role: String) : LoginUiEffect()
    data class ShowError(val message: String) : LoginUiEffect()
    object NavigateToLogin : LoginUiEffect()
}
