package com.ktx.dormitory.data.api

import com.ktx.dormitory.domain.model.BaseResponse
import com.ktx.dormitory.domain.model.Notification
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface NotificationApiService {
    @GET("notifications")
    suspend fun getNotifications(): BaseResponse<List<Notification>>

    @PUT("notifications/{id}/read")
    suspend fun markAsRead(@Path("id") id: String): BaseResponse<Unit>
}