package com.ktx.dormitory.data.remote.dto.auth

import com.google.gson.annotations.SerializedName

data class ChangePasswordRequest(
    @SerializedName("oldPassword") val oldPassword: String,
    @SerializedName("newPassword") val newPassword: String
)
