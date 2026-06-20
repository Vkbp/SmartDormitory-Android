package com.ktx.dormitory.data.remote.api

import com.ktx.dormitory.data.remote.dto.BaseResponse
import com.ktx.dormitory.data.remote.dto.notification.NotificationResponse
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Interface cho các API liên quan đến Thông báo
 */
interface NotificationApiService {
    @GET("v1/notifications")
    suspend fun getNotifications(): BaseResponse<List<NotificationResponse>>

    @POST("v1/notifications/{id}/read")
    suspend fun markAsRead(@Path("id") id: String): BaseResponse<Unit>
}
