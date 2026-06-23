package com.ktx.dormitory.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Model đại diện cho thông tin người dùng trong ứng dụng
 */
@Parcelize
data class UserData(
    val id: String? = null,        // UUID sinh viên - dùng để gọi /access/history/student/{id}
    val username: String,
    val role: String? = null,
    val fullName: String? = null
) : Parcelable

/**
 * Các lỗi liên quan đến xác thực
 */
sealed class AuthError : Exception() {
    object InvalidCredentials : AuthError()
    object NetworkError : AuthError()
    object SessionExpired : AuthError()
    data class UnknownError(override val message: String) : AuthError()
}
