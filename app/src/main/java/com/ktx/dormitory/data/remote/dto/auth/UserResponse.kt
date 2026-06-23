package com.ktx.dormitory.data.remote.dto.auth

import com.google.gson.annotations.SerializedName

/**
 * DTO cho thông tin tài khoản người dùng.
 * Tương thích với MeResponse.java từ Backend.
 */
data class UserResponse(
    @SerializedName("accountId") val id: String? = null,
    @SerializedName("username") val username: String,
    @SerializedName("email") val email: String? = null,
    @SerializedName("role") val role: String? = null,
    @SerializedName("status") val status: String? = null
)
