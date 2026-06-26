package com.ktx.dormitory.domain.settings.usecase

import com.ktx.dormitory.domain.settings.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFaceThresholdUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    operator fun invoke(): Flow<Float> = repository.getFaceThreshold()
}
