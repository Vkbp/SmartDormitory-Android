package com.ktx.dormitory.data.remote.api

import com.ktx.dormitory.data.remote.dto.BaseResponse
import com.ktx.dormitory.data.remote.dto.common.DormRequestDto
import retrofit2.http.*

/**
 * Interface cho các API liên quan đến Yêu cầu và Sửa chữa
 */
interface RequestApiService {
    @POST("v1/requests")
    suspend fun submitRequest(@Body request: DormRequestDto): BaseResponse<DormRequestDto>

    @GET("v1/requests/me")
    suspend fun getMyRequests(): BaseResponse<List<DormRequestDto>>

    @GET("v1/requests/pending")
    suspend fun getPendingRequests(): BaseResponse<List<DormRequestDto>>

    @PATCH("v1/requests/{id}/status")
    suspend fun updateRequestStatus(
        @Path("id") id: String,
        @Query("status") status: String
    ): BaseResponse<DormRequestDto>
}
