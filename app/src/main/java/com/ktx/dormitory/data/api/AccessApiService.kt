package com.ktx.dormitory.data.api

import com.ktx.dormitory.domain.model.AccessLog
import com.ktx.dormitory.domain.model.BaseResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AccessApiService {
    @POST("access/verify-qr")
    suspend fun verifyQr(@Body request: QrRequest): BaseResponse<Unit>

    @GET("access/history")
    suspend fun getAccessHistory(): BaseResponse<List<AccessLog>>

    // Gửi vector khuôn mặt lên Backend để cửa chính có thể nhận diện
    @POST("access/face/register")
    suspend fun registerFaceOnServer(@Body request: FaceRegisterRequest): BaseResponse<Unit>

    // Cho phép người quản lý mở cửa từ xa qua App
    @POST("access/remote-unlock")
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
