package com.ktx.dormitory.data.remote.api

import com.ktx.dormitory.data.remote.dto.BaseResponse
import com.ktx.dormitory.data.remote.dto.auth.*
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * Interface cho các API liên quan đến Xác thực
 */
interface AuthApiService {
    @POST("v1/auth/login")
    suspend fun login(@Body request: LoginRequest): BaseResponse<LoginResponse>

    @POST("v1/auth/refresh-token")
    suspend fun refreshToken(@Body request: RefreshTokenRequest): BaseResponse<LoginResponse>

    @GET("v1/users/me")
    suspend fun getCurrentUser(): BaseResponse<UserResponse>

    @POST("v1/auth/logout")
    suspend fun logout(): BaseResponse<Unit>

    @POST("v1/auth/change-password")
    suspend fun changePassword(@Body request: ChangePasswordRequest): BaseResponse<Unit>

    @POST("v1/auth/forgot-password")
    suspend fun forgotPassword(@Body request: ForgotPasswordRequest): BaseResponse<Unit>

    @POST("v1/auth/reset-password")
    suspend fun resetPassword(@Body request: ResetPasswordRequest): BaseResponse<Unit>
}
