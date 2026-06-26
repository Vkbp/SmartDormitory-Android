package com.ktx.dormitory.domain.profile.usecase

import com.ktx.dormitory.domain.profile.model.UpdateProfileRequest
import com.ktx.dormitory.domain.profile.repository.ProfileRepository
import javax.inject.Inject

class UpdateProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) {
    suspend operator fun invoke(fullName: String, phone: String, email: String): Result<Unit> {
        return profileRepository.updateProfile(
            UpdateProfileRequest(
                fullName = fullName,
                phone = phone,
                email = email
            )
        )
    }
}
