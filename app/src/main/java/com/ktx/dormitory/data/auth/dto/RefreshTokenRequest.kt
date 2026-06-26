package com.ktx.dormitory.data.auth.dto

import com.google.gson.annotations.SerializedName

data class RefreshTokenRequest(
    @SerializedName("refreshToken") val refreshToken: String
)
