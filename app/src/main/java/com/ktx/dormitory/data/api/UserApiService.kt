package com.ktx.dormitory.data.api

import com.ktx.dormitory.domain.model.*
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part

/**
 * Định nghĩa toàn bộ API nghiệp vụ người dùng theo Spec dòng 325 trở đi.
 */
interface UserApiService {

    // --- MODULE HỒ SƠ ---
    @GET("users/me")
    suspend fun getDetailedProfile(): BaseResponse<UserProfile>

    @PUT("users/me")
    suspend fun updateProfile(@Body request: UpdateProfileRequest): BaseResponse<Unit>

    @Multipart
    @POST("users/me/avatar")
    suspend fun uploadAvatar(@Part file: MultipartBody.Part): BaseResponse<String>


    // --- MODULE PHÒNG ---
    @GET("users/room")
    suspend fun getMyRoom(): BaseResponse<RoomInfo>


    // --- MODULE ĐƠN ĐĂNG KÝ ---
    @GET("users/applications/me")
    suspend fun getMyApplication(): BaseResponse<DormApplication>


    // --- MODULE THANH TOÁN (LỊCH SỬ) ---
    @GET("users/payments")
    suspend fun getPaymentHistory(): BaseResponse<List<Transaction>>

    // Lấy danh sách hóa đơn hiện tại
    @GET("users/bills")
    suspend fun getMyBills(): BaseResponse<List<Invoice>>
}
