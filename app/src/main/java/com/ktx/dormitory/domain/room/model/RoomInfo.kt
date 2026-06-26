package com.ktx.dormitory.domain.room.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Model thông tin phòng
 */
@Parcelize
data class RoomInfo(
    val building: String?,
    val floor: Int?,
    val roomCode: String?,
    val bedCode: String?,
    val status: String?
) : Parcelable
