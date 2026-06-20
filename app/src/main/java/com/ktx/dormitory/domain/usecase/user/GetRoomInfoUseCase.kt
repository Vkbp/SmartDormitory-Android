package com.ktx.dormitory.domain.usecase.user

import com.ktx.dormitory.domain.model.RoomInfo
import com.ktx.dormitory.domain.repository.UserRepository
import javax.inject.Inject

class GetRoomInfoUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Result<RoomInfo> {
        return userRepository.getRoomInfo()
    }
}
