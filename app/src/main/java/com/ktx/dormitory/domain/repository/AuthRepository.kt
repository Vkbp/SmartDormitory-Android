package com.ktx.dormitory.domain.repository

import com.ktx.dormitory.domain.model.LoginResponse
import com.ktx.dormitory.domain.model.UserData

/**
 * Interface định nghĩa các nghiệp vụ liên quan đến xác thực.
 */
interface AuthRepository {
    suspend fun login(username: String, password: String): Result<LoginResponse>
    suspend fun getCurrentUser(): Result<UserData>
    suspend fun changePassword(oldPass: String, newPass: String): Result<Unit>
    suspend fun forgotPassword(email: String): Result<Unit>
    suspend fun logout()
}
