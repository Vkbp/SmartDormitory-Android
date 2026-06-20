package com.ktx.dormitory.domain.repository

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun getFaceThreshold(): Flow<Float>
    suspend fun saveFaceThreshold(threshold: Float)
}
