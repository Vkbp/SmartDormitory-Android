package com.ktx.dormitory.data.access.remote

import com.ktx.dormitory.data.access.dto.AccessLogDto
import com.ktx.dormitory.data.common.dto.PageResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Header
import retrofit2.http.Path

interface AccessApiService {
    @POST("v1/students/me/face")
    suspend fun registerFaceOnServer(
        @Header("X-Student-Id") studentId: String,
        @Body request: HashMap<String, String>
    ): String

    @GET("v1/access/history/student/{id}")
    suspend fun getAccessHistory(@Path("id") studentId: String): PageResponse<AccessLogDto>
}
