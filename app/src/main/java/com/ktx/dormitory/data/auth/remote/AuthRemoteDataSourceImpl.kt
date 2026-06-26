package com.ktx.dormitory.data.auth.remote

import com.ktx.dormitory.data.remote.api.AuthApiService
import com.ktx.dormitory.data.remote.dto.auth.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRemoteDataSourceImpl @Inject constructor(
    private val api: AuthApiService
) : AuthRemoteDataSource {
    override suspend fun login(request: LoginRequest) = api.login(request)
    override suspend fun refreshToken(request: RefreshTokenRequest) = api.refreshToken(request)
    override suspend fun getCurrentUser() = api.getCurrentUser()
    override suspend fun logout() = api.logout()
    override suspend fun changePassword(request: ChangePasswordRequest) = api.changePassword(request)
    override suspend fun forgotPassword(request: ForgotPasswordRequest) = api.forgotPassword(request)
    override suspend fun resetPassword(request: ResetPasswordRequest) = api.resetPassword(request)
}
