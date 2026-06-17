package com.ktx.dormitory.data.repository

import com.ktx.dormitory.core.network.toUserFriendlyMessage
import com.ktx.dormitory.data.api.AccessApiService
import com.ktx.dormitory.data.api.QrRequest
import com.ktx.dormitory.data.local.AccessLogDao
import com.ktx.dormitory.data.local.toDomain
import com.ktx.dormitory.data.local.toEntity
import com.ktx.dormitory.domain.model.AccessLog
import com.ktx.dormitory.domain.repository.AccessRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccessRepositoryImpl @Inject constructor(
    private val api: AccessApiService,
    private val dao: AccessLogDao
) : AccessRepository {
    
    override val accessLogs: Flow<List<AccessLog>> = dao.getAllLogs().map { entities ->
        entities.map { it.toDomain() }
    }

    override suspend fun verifyQr(code: String): Result<Unit> {
        return try {
            val response = api.verifyQr(QrRequest(code))
            if (response.success) {
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(Exception(e.toUserFriendlyMessage()))
        }
    }

    override suspend fun getAccessHistory(): Result<List<AccessLog>> {
        return try {
            val response = api.getAccessHistory()
            if (response.success && response.data != null) {
                dao.insertLogs(response.data.map { it.toEntity() })
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(Exception(e.toUserFriendlyMessage()))
        }
    }
}
