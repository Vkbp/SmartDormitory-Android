package com.ktx.dormitory.data.remote.api

import com.ktx.dormitory.data.remote.dto.BaseResponse
import com.ktx.dormitory.data.remote.dto.common.AccessLogDto
import com.ktx.dormitory.data.remote.dto.common.FaceRegisterRequestDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * Interface cho các API liên quan đến Ra vào và AI
 */
interface AccessApiService {
    @POST("v1/access/verify-qr")
    suspend fun verifyQrCode(@Body qrCode: String): BaseResponse<Unit>

    @POST("v1/ai/register-face")
    suspend fun registerFaceOnServer(@Body request: FaceRegisterRequestDto): BaseResponse<Unit>

    @GET("v1/access/history")
    suspend fun getAccessHistory(): BaseResponse<List<AccessLogDto>>
}
