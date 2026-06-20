package com.ktx.dormitory.data.repository

import com.ktx.dormitory.core.network.toUserFriendlyMessage
import com.ktx.dormitory.data.local.datasource.AuthLocalDataSource
import com.ktx.dormitory.data.mapper.toDomain
import com.ktx.dormitory.data.remote.datasource.AuthRemoteDataSource
import com.ktx.dormitory.data.remote.dto.auth.*
import com.ktx.dormitory.domain.model.*
import com.ktx.dormitory.domain.repository.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val remoteDataSource: AuthRemoteDataSource,
    private val localDataSource: AuthLocalDataSource
) : AuthRepository {

    override suspend fun login(usernameOrEmail: String, password: String): Result<UserData> {
        return try {
            localDataSource.clearTokens()
            
            val response = remoteDataSource.login(LoginRequest(usernameOrEmail, password))
            if (response.success && response.data != null) {
                localDataSource.saveTokens(response.data.accessToken, response.data.refreshToken)
                val user = UserData(
                    username = usernameOrEmail,
                    role = response.data.role,
                    fullName = null
                )
                Result.success(user)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(Exception(e.toUserFriendlyMessage()))
        }
    }

    override suspend fun getCurrentUser(): Result<UserData> {
        return try {
            val response = remoteDataSource.getCurrentUser()
            if (response.success && response.data != null) {
                Result.success(response.data.toDomain())
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(Exception(e.toUserFriendlyMessage()))
        }
    }

    override suspend fun changePassword(oldPass: String, newPass: String): Result<Unit> {
        return try {
            val response = remoteDataSource.changePassword(ChangePasswordRequest(oldPass, newPass))
            if (response.success) Result.success(Unit) else Result.failure(Exception(response.message))
        } catch (e: Exception) { 
            Result.failure(Exception(e.toUserFriendlyMessage())) 
        }
    }

    override suspend fun forgotPassword(email: String): Result<Unit> {
        return try {
            val response = remoteDataSource.forgotPassword(ForgotPasswordRequest(email))
            if (response.success) Result.success(Unit) else Result.failure(Exception(response.message))
        } catch (e: Exception) { 
            Result.failure(Exception(e.toUserFriendlyMessage())) 
        }
    }

    override suspend fun resetPassword(token: String, newPass: String): Result<Unit> {
        return try {
            val response = remoteDataSource.resetPassword(ResetPasswordRequest(token, newPass))
            if (response.success) Result.success(Unit) else Result.failure(Exception(response.message))
        } catch (e: Exception) { 
            Result.failure(Exception(e.toUserFriendlyMessage())) 
        }
    }

    override suspend fun logout() {
        try {
            remoteDataSource.logout()
            localDataSource.clearTokens(keepRefreshToken = false)
        } catch (e: Exception) {
            localDataSource.clearTokens(keepRefreshToken = false)
        }
    }
}

