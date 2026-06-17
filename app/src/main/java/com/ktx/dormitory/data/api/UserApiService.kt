package com.ktx.dormitory.data.api

import com.ktx.dormitory.domain.model.*
import okhttp3.MultipartBody
import retrofit2.http.*

/**
 * Đã chuẩn hóa toàn bộ Endpoint theo API_DOCUMENTATION.md
 * Sử dụng PATCH cho cập nhật hồ sơ và chuẩn hóa Base Path.
 */
interface UserApiService {

    // --- MODULE HỒ SƠ (STUDENT) ---
    @GET("v1/students/me")
    suspend fun getDetailedProfile(): BaseResponse<UserProfile>

    @PATCH("v1/students/me") // Sử dụng PATCH theo tài liệu
    suspend fun updateProfile(@Body request: UpdateProfileRequest): BaseResponse<UserProfile>

    @Multipart
    @POST("v1/uploads/avatar") // Chuẩn hóa đường dẫn Upload
    suspend fun uploadAvatar(@Part file: MultipartBody.Part): BaseResponse<String>


    // --- MODULE PHÒNG (Dữ liệu bổ sung không có trong Doc Auth nhưng có trong mã nguồn) ---
    @GET("v1/users/room")
    suspend fun getMyRoom(): BaseResponse<RoomInfo>


    // --- MODULE ĐƠN ĐĂNG KÝ ---
    @GET("v1/users/applications/me")
    suspend fun getMyApplication(): BaseResponse<DormApplication>


    // --- MODULE THANH TOÁN ---
    @GET("v1/users/payments")
    suspend fun getPaymentHistory(): BaseResponse<List<Transaction>>

    @GET("v1/users/bills")
    suspend fun getMyBills(): BaseResponse<List<Invoice>>
}
