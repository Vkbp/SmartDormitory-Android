package com.ktx.dormitory.data.api

import com.ktx.dormitory.domain.model.BaseResponse
import com.ktx.dormitory.domain.model.ChangePasswordRequest
import com.ktx.dormitory.domain.model.ForgotPasswordRequest
import com.ktx.dormitory.domain.model.LoginRequest
import com.ktx.dormitory.domain.model.LoginResponse
import com.ktx.dormitory.domain.model.RefreshRequest
import com.ktx.dormitory.domain.model.UserData
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApiService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): BaseResponse<LoginResponse>

    @POST("auth/refresh-token")
    suspend fun refreshToken(@Body request: RefreshRequest): BaseResponse<LoginResponse>

    // Thêm vào interface AuthApiService
    @GET("auth/me")
    suspend fun getCurrentUser(): BaseResponse<UserData>

    @POST("auth/logout")
    suspend fun logout(): BaseResponse<Unit>

    @POST("auth/change-password")
    suspend fun changePassword(@Body request: ChangePasswordRequest): BaseResponse<Unit>

    @POST("auth/forgot-password")
    suspend fun forgotPassword(@Body request: ForgotPasswordRequest): BaseResponse<Unit>


}