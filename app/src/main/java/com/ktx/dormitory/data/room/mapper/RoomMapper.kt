package com.ktx.dormitory.data.room.mapper

import com.ktx.dormitory.data.room.dto.RoomInfoDto
import com.ktx.dormitory.domain.room.model.RoomInfo

fun RoomInfoDto.toDomain(): RoomInfo {
    return RoomInfo(
        building = building,
        floor = floor,
        roomCode = roomCode,
        bedCode = bedCode,
        status = status
    )
}
