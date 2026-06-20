package com.ktx.dormitory.data.remote.dto.auth

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("username") val username: String,
    @SerializedName("role") val role: String? = null,
    @SerializedName("fullName", alternate = ["full_name"]) val fullName: String? = null
)
