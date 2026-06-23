package com.ktx.dormitory.data.remote.api

import com.ktx.dormitory.data.remote.dto.BaseResponse
import com.ktx.dormitory.data.remote.dto.common.AccessLogDto

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * Interface cho các API liên quan đến Ra vào và AI
 */
interface AccessApiService {
    @POST("v1/students/me/face")
    suspend fun registerFaceOnServer(
        @retrofit2.http.Header("X-Student-Id") studentId: String,
        @Body request: HashMap<String, String>
    ): String

    @GET("v1/access/history/student/{id}")
    suspend fun getAccessHistory(@retrofit2.http.Path("id") studentId: String): com.ktx.dormitory.data.remote.dto.common.PageResponse<AccessLogDto>
}
