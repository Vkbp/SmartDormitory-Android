package com.ktx.dormitory.data.repository

import com.ktx.dormitory.core.utils.DataStoreManager
import com.ktx.dormitory.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepositoryImpl @Inject constructor(
    private val dataStoreManager: DataStoreManager
) : SettingsRepository {
    override fun getFaceThreshold(): Flow<Float> = dataStoreManager.faceThreshold
    override suspend fun saveFaceThreshold(threshold: Float) = dataStoreManager.saveThreshold(threshold)
}
