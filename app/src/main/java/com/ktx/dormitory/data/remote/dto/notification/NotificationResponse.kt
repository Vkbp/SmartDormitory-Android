package com.ktx.dormitory.data.remote.dto.notification

import com.google.gson.annotations.SerializedName

data class NotificationResponse(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String?,
    @SerializedName("content") val content: String?,
    @SerializedName("type") val type: String?,
    @SerializedName("createdAt") val createdAt: String?,
    @SerializedName("isRead") val isRead: Boolean = false
)
