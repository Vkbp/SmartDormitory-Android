package com.ktx.dormitory.data.auth.remote

import com.ktx.dormitory.data.auth.dto.*
import com.ktx.dormitory.data.common.dto.BaseResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApiService {
    @POST("v1/auth/login")
    suspend fun login(@Body request: LoginRequest): BaseResponse<LoginResponse>

    @POST("v1/auth/refresh")
    suspend fun refreshToken(@Body request: RefreshTokenRequest): BaseResponse<LoginResponse>

    @GET("v1/students/me")
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
