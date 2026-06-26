package com.ktx.dormitory.domain.profile.usecase

import com.ktx.dormitory.domain.profile.repository.ProfileRepository
import javax.inject.Inject

class UploadAvatarUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) {
    suspend operator fun invoke(filePath: String): Result<String> {
        return profileRepository.uploadAvatar(filePath)
    }
}
