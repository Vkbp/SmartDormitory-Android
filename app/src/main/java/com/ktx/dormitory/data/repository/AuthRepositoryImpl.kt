package com.ktx.dormitory.data.repository

import com.ktx.dormitory.core.network.toUserFriendlyMessage
import com.ktx.dormitory.data.api.AuthApiService
import com.ktx.dormitory.data.local.TokenManager
import com.ktx.dormitory.domain.model.*
import com.ktx.dormitory.domain.repository.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApiService,
    private val tokenManager: TokenManager
) : AuthRepository {

    override suspend fun login(usernameOrEmail: String, password: String): Result<LoginResponse> {
        return try {
            // Xóa token cũ trước khi login mới
            tokenManager.clearTokens()
            
            val response = api.login(LoginRequest(usernameOrEmail, password))
            if (response.success && response.data != null) {
                tokenManager.saveTokens(response.data.accessToken, response.data.refreshToken)
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(Exception(e.toUserFriendlyMessage()))
        }
    }

    override suspend fun getCurrentUser(): Result<UserData> {
        return try {
            val response = api.getCurrentUser()
            if (response.success && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(Exception(e.toUserFriendlyMessage()))
        }
    }

    override suspend fun changePassword(oldPass: String, newPass: String): Result<Unit> {
        return try {
            val response = api.changePassword(ChangePasswordRequest(oldPass, newPass))
            if (response.success) Result.success(Unit) else Result.failure(Exception(response.message))
        } catch (e: Exception) { 
            Result.failure(Exception(e.toUserFriendlyMessage())) 
        }
    }

    override suspend fun forgotPassword(email: String): Result<Unit> {
        return try {
            val response = api.forgotPassword(ForgotPasswordRequest(email))
            if (response.success) Result.success(Unit) else Result.failure(Exception(response.message))
        } catch (e: Exception) { 
            Result.failure(Exception(e.toUserFriendlyMessage())) 
        }
    }

    override suspend fun resetPassword(token: String, newPass: String): Result<Unit> {
        return try {
            val response = api.resetPassword(ResetPasswordRequest(token, newPass))
            if (response.success) Result.success(Unit) else Result.failure(Exception(response.message))
        } catch (e: Exception) { 
            Result.failure(Exception(e.toUserFriendlyMessage())) 
        }
    }

    override suspend fun logout() {
        try {
            api.logout()
            tokenManager.clearTokens(keepRefreshToken = true)
        } catch (e: Exception) {
            tokenManager.clearTokens(keepRefreshToken = true)
        }
    }
}
