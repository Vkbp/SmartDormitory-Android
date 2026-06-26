package com.ktx.dormitory.domain.application.usecase

import com.ktx.dormitory.domain.application.model.DormApplication
import com.ktx.dormitory.domain.application.repository.ApplicationRepository
import javax.inject.Inject

class GetApplicationTimelineUseCase @Inject constructor(
    private val applicationRepository: ApplicationRepository
) {
    suspend operator fun invoke(): Result<DormApplication> {
        return applicationRepository.getApplicationTimeline()
    }
}
