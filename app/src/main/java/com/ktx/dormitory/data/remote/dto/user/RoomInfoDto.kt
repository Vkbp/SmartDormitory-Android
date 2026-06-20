package com.ktx.dormitory.data.remote.dto.user

import com.google.gson.annotations.SerializedName

data class RoomInfoDto(
    @SerializedName("building") val building: String?,
    @SerializedName("floor") val floor: String?,
    @SerializedName("room_code") val roomCode: String?,
    @SerializedName("bed_code") val bedCode: String?,
    @SerializedName("status") val status: String?
)
