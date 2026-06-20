package com.ktx.dormitory.data.remote.api

import com.ktx.dormitory.data.remote.dto.BaseResponse
import com.ktx.dormitory.data.remote.dto.user.*
import okhttp3.MultipartBody
import retrofit2.http.*

/**
 * Interface cho các API liên quan đến Người dùng và Sinh viên
 */
interface UserApiService {

    @GET("v1/students/me")
    suspend fun getDetailedProfile(): BaseResponse<StudentResponse>

    @PATCH("v1/students/me")
    suspend fun updateProfile(@Body request: UpdateProfileRequest): BaseResponse<StudentResponse>

    @Multipart
    @POST("v1/uploads/avatar")
    suspend fun uploadAvatar(@Part file: MultipartBody.Part): BaseResponse<String>

    @GET("v1/users/room")
    suspend fun getMyRoom(): BaseResponse<RoomInfoDto>

    @GET("v1/users/applications/me")
    suspend fun getMyApplication(): BaseResponse<DormApplicationDto>

    @GET("v1/users/payments")
    suspend fun getPaymentHistory(): BaseResponse<List<TransactionDto>>

    @GET("v1/users/bills")
    suspend fun getMyBills(): BaseResponse<List<InvoiceDto>>
}
