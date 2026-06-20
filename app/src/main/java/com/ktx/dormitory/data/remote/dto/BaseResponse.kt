package com.ktx.dormitory.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * DTO cho phản hồi chung từ API
 */
data class BaseResponse<T>(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: T?
)
