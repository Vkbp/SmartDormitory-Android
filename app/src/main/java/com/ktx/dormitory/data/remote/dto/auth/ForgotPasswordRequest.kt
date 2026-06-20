package com.ktx.dormitory.data.remote.dto.auth

import com.google.gson.annotations.SerializedName

data class ForgotPasswordRequest(
    @SerializedName("email") val email: String
)
