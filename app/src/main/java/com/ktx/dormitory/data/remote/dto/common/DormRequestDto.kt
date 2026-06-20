package com.ktx.dormitory.data.remote.dto.common

import com.google.gson.annotations.SerializedName

data class DormRequestDto(
    @SerializedName("id") val id: String,
    @SerializedName("studentName") val studentName: String?,
    @SerializedName("studentId") val studentId: String?,
    @SerializedName("type") val type: String?,
    @SerializedName("content") val content: String?,
    @SerializedName("status") val status: String,
    @SerializedName("createdAt") val createdAt: String?
)

data class FaceRegisterRequestDto(
    @SerializedName("studentId") val studentId: String,
    @SerializedName("embedding") val embedding: List<Float>
)
