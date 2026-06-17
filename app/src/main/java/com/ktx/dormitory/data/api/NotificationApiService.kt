package com.ktx.dormitory.data.api

import com.ktx.dormitory.domain.model.BaseResponse
import com.ktx.dormitory.domain.model.Notification
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

/**
 * Đã chuẩn hóa Endpoint theo v1/
 */
interface NotificationApiService {
    @GET("v1/notifications")
    suspend fun getNotifications(): BaseResponse<List<Notification>>

    @PUT("v1/notifications/{id}/read")
    suspend fun markAsRead(@Path("id") id: String): BaseResponse<Unit>
}
