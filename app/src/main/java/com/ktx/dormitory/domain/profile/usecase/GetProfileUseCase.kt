package com.ktx.dormitory.domain.profile.usecase

import com.ktx.dormitory.domain.profile.model.UserProfile
import com.ktx.dormitory.domain.profile.repository.ProfileRepository
import javax.inject.Inject

class GetProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) {
    suspend operator fun invoke(): Result<UserProfile> {
        return profileRepository.getProfile()
    }
}
