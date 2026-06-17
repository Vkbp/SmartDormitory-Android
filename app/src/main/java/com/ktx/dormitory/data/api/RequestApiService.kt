package com.ktx.dormitory.data.api

import com.ktx.dormitory.domain.model.BaseResponse
import com.ktx.dormitory.domain.model.DormRequest
import com.ktx.dormitory.domain.model.RequestStatus
import com.ktx.dormitory.domain.model.RequestType
import retrofit2.http.*

/**
 * Đã chuẩn hóa Endpoint theo v1/
 */
interface RequestApiService {
    @GET("v1/requests/my")
    suspend fun getMyRequests(): BaseResponse<List<DormRequest>>

    @POST("v1/requests")
    suspend fun createRequest(@Body request: CreateDormRequest): BaseResponse<Unit>

    @GET("v1/requests/all")
    suspend fun getAllRequests(): BaseResponse<List<DormRequest>>

    @PATCH("v1/requests/{id}/status")
    suspend fun updateRequestStatus(
        @Path("id") id: String,
        @Body statusRequest: UpdateStatusRequest
    ): BaseResponse<Unit>
}

data class CreateDormRequest(val type: RequestType, val content: String)
data class UpdateStatusRequest(val status: RequestStatus)
