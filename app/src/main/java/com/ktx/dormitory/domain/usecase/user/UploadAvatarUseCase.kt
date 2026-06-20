package com.ktx.dormitory.domain.usecase.user

import com.ktx.dormitory.domain.repository.UserRepository
import javax.inject.Inject

class UploadAvatarUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(filePath: String): Result<String> {
        return userRepository.uploadAvatar(filePath)
    }
}
