package com.ktx.dormitory.data.extension.repository

import com.ktx.dormitory.domain.extension.repository.ExtensionRepository
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExtensionRepositoryImpl @Inject constructor() : ExtensionRepository {
    override suspend fun requestExtension(studentId: String, reason: String): Result<Unit> {
        // TODO: Implement actual API call
        delay(1000)
        return Result.success(Unit)
    }
}
