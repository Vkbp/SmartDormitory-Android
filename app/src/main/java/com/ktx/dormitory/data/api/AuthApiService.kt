package com.ktx.dormitory.data.api

import com.ktx.dormitory.domain.model.*
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * Đã chuẩn hóa toàn bộ Endpoint theo API_DOCUMENTATION.md
 */
interface AuthApiService {
    @POST("v1/auth/login")
    suspend fun login(@Body request: LoginRequest): BaseResponse<LoginResponse>

    @POST("v1/auth/refresh-token")
    suspend fun refreshToken(@Body request: RefreshTokenRequest): BaseResponse<LoginResponse>

    @GET("v1/users/me") // Theo mục User trong Doc
    suspend fun getCurrentUser(): BaseResponse<UserData>

    @POST("v1/auth/logout")
    suspend fun logout(): BaseResponse<Unit>

    @POST("v1/auth/change-password")
    suspend fun changePassword(@Body request: ChangePasswordRequest): BaseResponse<Unit>

    @POST("v1/auth/forgot-password")
    suspend fun forgotPassword(@Body request: ForgotPasswordRequest): BaseResponse<Unit>

    @POST("v1/auth/reset-password")
    suspend fun resetPassword(@Body request: ResetPasswordRequest): BaseResponse<Unit>
}
