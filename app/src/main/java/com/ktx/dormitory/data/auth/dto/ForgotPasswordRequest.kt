package com.ktx.dormitory.data.auth.dto

import com.google.gson.annotations.SerializedName

data class ForgotPasswordRequest(
    @SerializedName("email") val email: String
)
