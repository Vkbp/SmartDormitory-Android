package com.ktx.dormitory.data.api

import com.ktx.dormitory.domain.model.AccessLog
import com.ktx.dormitory.domain.model.BaseResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * Đã chuẩn hóa Endpoint theo v1/
 */
interface AccessApiService {
    @POST("v1/access/verify-qr")
    suspend fun verifyQr(@Body request: QrRequest): BaseResponse<Unit>

    @GET("v1/access/history")
    suspend fun getAccessHistory(): BaseResponse<List<AccessLog>>

    @POST("v1/access/face/register")
    suspend fun registerFaceOnServer(@Body request: FaceRegisterRequest): BaseResponse<Unit>

    @POST("v1/access/remote-unlock")
    suspend fun remoteUnlock(@Body request: UnlockRequest): BaseResponse<Unit>
}

data class QrRequest(val qrCode: String)

data class FaceRegisterRequest(
    val studentId: String,
    val faceVector: List<Float>
)

data class UnlockRequest(
    val doorId: String,
    val reason: String
)
