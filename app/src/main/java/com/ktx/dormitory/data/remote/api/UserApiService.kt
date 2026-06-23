package com.ktx.dormitory.data.remote.api

import com.ktx.dormitory.data.remote.dto.BaseResponse
import com.ktx.dormitory.data.remote.dto.user.*
import okhttp3.MultipartBody
import retrofit2.http.*

/**
 * Interface cho các API liên quan đến Người dùng và Sinh viên.
 * Đồng bộ với Backend endpoints:
 *   GET  v1/students/me          - Lấy profile sinh viên
 *   PATCH v1/students/me         - Cập nhật profile
 *   POST v1/uploads/avatar       - Upload avatar
 *   GET  v1/student/room/current - Thông tin phòng hiện tại
 *   GET  v1/applications/status  - Đơn đăng ký ký túc xá (theo CCCD)
 *   GET  v1/bills                - Lịch sử hóa đơn/thanh toán
 */
interface UserApiService {

    @GET("v1/students/me")
    suspend fun getDetailedProfile(): BaseResponse<StudentResponse>

    @PATCH("v1/students/me")
    suspend fun updateProfile(@Body request: UpdateProfileRequest): BaseResponse<StudentResponse>

    @Multipart
    @POST("v1/uploads/avatar")
    suspend fun uploadAvatar(@Part file: MultipartBody.Part): BaseResponse<String>

    @GET("v1/student/room/current")
    suspend fun getMyRoom(): BaseResponse<RoomInfoDto>

    /**
     * Tra cứu đơn đăng ký. Backend sử dụng endpoint /api/v1/applications/status?cccd=...
     */
    @GET("v1/applications/status")
    suspend fun getMyApplication(@Query("cccd") cccd: String): BaseResponse<DormApplicationDto>

    /**
     * Lấy lịch sử thanh toán. 
     * Do Backend không có endpoint /users/payments, chuyển hướng sang /v1/bills
     */
    @GET("v1/bills")
    suspend fun getPaymentHistory(): BaseResponse<List<TransactionDto>>
}
