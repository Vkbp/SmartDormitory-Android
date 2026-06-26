package com.ktx.dormitory.data.application.remote

import com.ktx.dormitory.data.common.dto.BaseResponse
import com.ktx.dormitory.data.application.dto.DormApplicationDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ApplicationApiService {
    @GET("v1/applications/status")
    suspend fun getMyApplication(@Query("cccd") cccd: String): BaseResponse<DormApplicationDto>
}
