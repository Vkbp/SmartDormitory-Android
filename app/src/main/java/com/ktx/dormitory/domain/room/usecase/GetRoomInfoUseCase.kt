package com.ktx.dormitory.domain.room.usecase

import com.ktx.dormitory.domain.room.model.RoomInfo
import com.ktx.dormitory.domain.room.repository.RoomRepository
import javax.inject.Inject

class GetRoomInfoUseCase @Inject constructor(
    private val roomRepository: RoomRepository
) {
    suspend operator fun invoke(): Result<RoomInfo> {
        return roomRepository.getRoomInfo()
    }
}
