package com.ktx.dormitory.domain.repository

import com.ktx.dormitory.domain.model.AccessLog
import kotlinx.coroutines.flow.Flow

interface AccessRepository {
    val accessLogs: Flow<List<AccessLog>>
    suspend fun verifyQr(code: String): Result<Unit>
    suspend fun getAccessHistory(): Result<List<AccessLog>>
}
