package com.ktx.dormitory.data.remote.dto.auth

import com.google.gson.annotations.SerializedName

data class ResetPasswordRequest(
    @SerializedName("token") val token: String,
    @SerializedName("newPassword") val newPassword: String
)
