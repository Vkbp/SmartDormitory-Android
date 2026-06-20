package com.ktx.dormitory.data.remote.datasource

import com.ktx.dormitory.data.remote.dto.BaseResponse
import com.ktx.dormitory.data.remote.dto.auth.*

interface AuthRemoteDataSource {
    suspend fun login(request: LoginRequest): BaseResponse<LoginResponse>
    suspend fun refreshToken(request: RefreshTokenRequest): BaseResponse<LoginResponse>
    suspend fun getCurrentUser(): BaseResponse<UserResponse>
    suspend fun logout(): BaseResponse<Unit>
    suspend fun changePassword(request: ChangePasswordRequest): BaseResponse<Unit>
    suspend fun forgotPassword(request: ForgotPasswordRequest): BaseResponse<Unit>
    suspend fun resetPassword(request: ResetPasswordRequest): BaseResponse<Unit>
}
