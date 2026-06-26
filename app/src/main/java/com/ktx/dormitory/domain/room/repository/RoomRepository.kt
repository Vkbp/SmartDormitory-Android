package com.ktx.dormitory.domain.room.repository

import com.ktx.dormitory.domain.room.model.RoomInfo

interface RoomRepository {
    suspend fun getRoomInfo(): Result<RoomInfo>
}
