package com.ktx.dormitory.data.auth.repository

import com.ktx.dormitory.core.network.toUserFriendlyMessage
import com.ktx.dormitory.data.auth.local.AuthLocalDataSource
import com.ktx.dormitory.data.auth.mapper.toDomain
import com.ktx.dormitory.data.auth.remote.AuthRemoteDataSource
import com.ktx.dormitory.data.auth.dto.*
import com.ktx.dormitory.domain.auth.model.UserData
import com.ktx.dormitory.domain.auth.repository.AuthRepository
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
                val profileResponse = remoteDataSource.getCurrentUser()
                
                if (profileResponse.success && profileResponse.data != null) {
                    val user = profileResponse.data.toDomain()
                    Result.success(user)
                } else {
                    val fallbackUser = UserData(
                        username = usernameOrEmail,
                        role = "STUDENT"
                    )
                    Result.success(fallbackUser)
                }
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
            localDataSource.saveLoginStatus(false)
        } catch (e: Exception) {
            localDataSource.clearTokens(keepRefreshToken = false)
            localDataSource.saveLoginStatus(false)
        }
    }

    override suspend fun saveLoginStatus(isLoggedIn: Boolean) {
        localDataSource.saveLoginStatus(isLoggedIn)
    }
}
