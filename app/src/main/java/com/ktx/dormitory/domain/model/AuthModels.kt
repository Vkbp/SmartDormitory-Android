package com.ktx.dormitory.domain.model

import com.google.gson.annotations.SerializedName

// Format chung của API (Chương 7)
data class BaseResponse<T>(
    val success: Boolean,
    val message: String,
    val data: T?
)

// Dữ liệu Login (Chương 10)
data class LoginRequest(val username: String, val password: String)

data class LoginResponse(
    val accessToken: String,
    val refreshToken: String,
    val role: String
)

// Dữ liệu Refresh Token (Chương 13)
data class RefreshRequest(val refreshToken: String)

// Đảm bảo class UserData có đầy đủ các trường này
data class UserData(
    val username: String,
    val role: String,
    @SerializedName("fullName", alternate = ["full_name"]) val fullName: String? = null // Cho phép Null vì API auth/me có thể không trả về tên
)

// Dữ liệu cho đổi mật khẩu (Chương 12)
data class ChangePasswordRequest(
    val oldPassword: String,
    val newPassword: String
)

// Dữ liệu cho quên mật khẩu (Chương 15)
data class ForgotPasswordRequest(
    val email: String
)


