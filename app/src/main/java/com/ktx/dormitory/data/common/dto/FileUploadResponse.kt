package com.ktx.dormitory.data.common.dto

import com.google.gson.annotations.SerializedName

data class FileUploadResponse(
    @SerializedName("url") val url: String
)
