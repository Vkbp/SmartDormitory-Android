package com.ktx.dormitory.data.api

import com.ktx.dormitory.domain.model.BaseResponse
import com.ktx.dormitory.domain.model.DormRequest
import com.ktx.dormitory.domain.model.RequestStatus
import com.ktx.dormitory.domain.model.RequestType
import retrofit2.http.*

interface RequestApiService {
    @GET("requests/my-requests")
    suspend fun getMyRequests(): BaseResponse<List<DormRequest>>

    @POST("requests/create")
    suspend fun createRequest(@Body request: CreateDormRequest): BaseResponse<Unit>

    @GET("requests/all")
    suspend fun getAllRequests(): BaseResponse<List<DormRequest>>

    @PATCH("requests/{id}/status")
    suspend fun updateRequestStatus(
        @Path("id") id: String,
        @Body statusRequest: UpdateStatusRequest
    ): BaseResponse<Unit>
}

data class CreateDormRequest(val type: RequestType, val content: String)
data class UpdateStatusRequest(val status: RequestStatus)
