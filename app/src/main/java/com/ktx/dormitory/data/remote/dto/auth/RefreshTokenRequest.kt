package com.ktx.dormitory.data.remote.dto.auth

import com.google.gson.annotations.SerializedName

data class RefreshTokenRequest(
    @SerializedName("refreshToken") val refreshToken: String
)
