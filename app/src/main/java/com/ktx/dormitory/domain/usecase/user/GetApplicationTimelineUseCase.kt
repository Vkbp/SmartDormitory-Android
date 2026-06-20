package com.ktx.dormitory.domain.usecase.user

import com.ktx.dormitory.domain.model.DormApplication
import com.ktx.dormitory.domain.repository.UserRepository
import javax.inject.Inject

class GetApplicationTimelineUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Result<DormApplication> {
        return userRepository.getApplicationTimeline()
    }
}
