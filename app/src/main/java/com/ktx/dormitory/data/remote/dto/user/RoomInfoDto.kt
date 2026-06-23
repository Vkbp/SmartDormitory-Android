package com.ktx.dormitory.data.remote.dto.user

import com.google.gson.annotations.SerializedName

data class RoomInfoDto(
    @SerializedName("buildingName") val building: String?,
    @SerializedName("floorNumber") val floor: Int?,
    @SerializedName("roomCode") val roomCode: String?,
    @SerializedName("bedCode") val bedCode: String?,
    @SerializedName("roomStatus") val status: String?
)
