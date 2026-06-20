package com.ktx.dormitory.domain.usecase.user

import com.ktx.dormitory.domain.model.UserProfile
import com.ktx.dormitory.domain.repository.UserRepository
import javax.inject.Inject

class GetProfileUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Result<UserProfile> {
        return userRepository.getProfile()
    }
}
