package com.ktx.dormitory.domain.settings.usecase

import com.ktx.dormitory.domain.settings.repository.SettingsRepository
import javax.inject.Inject

class SaveFaceThresholdUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke(threshold: Float) = repository.saveFaceThreshold(threshold)
}
