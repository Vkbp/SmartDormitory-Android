package com.ktx.dormitory.data.remote.dto.common

import com.google.gson.annotations.SerializedName

data class AccessLogDto(
    @SerializedName("id") val id: String,
    @SerializedName("studentId") val studentId: String?,
    @SerializedName("gateId") val gateId: String?,
    @SerializedName("buildingId") val buildingId: String?,
    @SerializedName("eventTimestamp") val eventTimestamp: String?,
    @SerializedName("decision") val decision: String?,
    @SerializedName("denialReason") val denialReason: String?,
    @SerializedName("method") val method: String?
)
