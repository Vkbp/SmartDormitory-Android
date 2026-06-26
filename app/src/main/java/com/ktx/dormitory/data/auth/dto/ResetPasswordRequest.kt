package com.ktx.dormitory.data.auth.dto

import com.google.gson.annotations.SerializedName

data class ResetPasswordRequest(
    @SerializedName("token") val token: String,
    @SerializedName("newPassword") val newPassword: String
)
