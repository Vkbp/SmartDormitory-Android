package com.ktx.dormitory.data.common.dto

import com.google.gson.annotations.SerializedName

data class PageResponse<T>(
    @SerializedName("content") val content: List<T>,
    @SerializedName("totalElements") val totalElements: Long,
    @SerializedName("totalPages") val totalPages: Int,
    @SerializedName("size") val size: Int,
    @SerializedName("number") val number: Int
)
