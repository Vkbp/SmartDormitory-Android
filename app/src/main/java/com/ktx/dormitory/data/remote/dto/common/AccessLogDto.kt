package com.ktx.dormitory.data.remote.dto.common

import com.google.gson.annotations.SerializedName

data class AccessLogDto(
    @SerializedName("id") val id: String,
    @SerializedName("userId") val userId: String?,
    @SerializedName("location") val location: String?,
    @SerializedName("timestamp") val timestamp: String?,
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("method") val method: String?
)
