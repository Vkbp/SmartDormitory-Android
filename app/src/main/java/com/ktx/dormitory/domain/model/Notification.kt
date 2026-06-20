package com.ktx.dormitory.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Notification(
    val id: String,
    val title: String?,
    val message: String?,
    val time: String?,
    val isRead: Boolean
) : Parcelable
