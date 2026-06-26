package com.ktx.dormitory.domain.extension.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ExtensionRequest(
    val id: String? = null,
    val studentId: String,
    val reason: String,
    val status: String? = "PENDING",
    val createdAt: String? = null
) : Parcelable
