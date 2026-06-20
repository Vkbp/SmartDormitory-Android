package com.ktx.dormitory.data.repository

import com.google.gson.Gson
import com.ktx.dormitory.core.network.NetworkMonitor
import com.ktx.dormitory.core.sync.RegisterFacePayload
import com.ktx.dormitory.core.sync.SyncScheduler
import com.ktx.dormitory.data.local.dao.AccessLogDao
import com.ktx.dormitory.data.local.dao.PendingSyncDao
import com.ktx.dormitory.data.local.entity.PendingSyncEntity
import com.ktx.dormitory.data.local.toDomain
import com.ktx.dormitory.data.local.toEntity
import com.ktx.dormitory.data.remote.datasource.AccessRemoteDataSource
import com.ktx.dormitory.data.mapper.toDomain
import com.ktx.dormitory.data.remote.dto.common.FaceRegisterRequestDto
import com.ktx.dormitory.domain.model.AccessLog
import com.ktx.dormitory.domain.repository.AccessRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccessRepositoryImpl @Inject constructor(
    private val remoteDataSource: AccessRemoteDataSource,
    private val localDao: AccessLogDao,
    private val pendingSyncDao: PendingSyncDao,
    private val syncScheduler: SyncScheduler,
    private val networkMonitor: NetworkMonitor
) : AccessRepository {

    override val accessLogs: Flow<List<AccessLog>> = localDao.getAllLogs()
        .map { list -> list.map { it.toDomain() } }

    private val gson = Gson()

    override suspend fun verifyQrCode(qrCode: String): Result<Unit> {
        return try {
            val isOnline = networkMonitor.isOnline.first()
            if (!isOnline) {
                return queueAction("VERIFY_QR", qrCode)
            }
            val response = remoteDataSource.verifyQrCode(qrCode)
            if (response.success) Result.success(Unit) else Result.failure(Exception(response.message))
        } catch (e: Exception) {
            if (isNetworkException(e)) {
                queueAction("VERIFY_QR", qrCode)
            } else {
                Result.failure(e)
            }
        }
    }

    override suspend fun registerFaceOnServer(studentId: String, embedding: List<Float>): Result<Unit> {
        val payload = gson.toJson(RegisterFacePayload(studentId, embedding))
        return try {
            val isOnline = networkMonitor.isOnline.first()
            if (!isOnline) {
                return queueAction("REGISTER_FACE", payload)
            }
            val response = remoteDataSource.registerFaceOnServer(FaceRegisterRequestDto(studentId, embedding))
            if (response.success) Result.success(Unit) else Result.failure(Exception(response.message))
        } catch (e: Exception) {
            if (isNetworkException(e)) {
                queueAction("REGISTER_FACE", payload)
            } else {
                Result.failure(e)
            }
        }
    }

    private suspend fun queueAction(actionType: String, payload: String): Result<Unit> {
        pendingSyncDao.insertAction(
            PendingSyncEntity(
                actionType = actionType,
                payload = payload
            )
        )
        syncScheduler.scheduleSync()
        return Result.success(Unit)
    }

    private fun isNetworkException(e: Exception): Boolean {
        return e is java.io.IOException
    }

    override suspend fun getAccessHistory(): Result<List<AccessLog>> {
        return try {
            val response = remoteDataSource.getAccessHistory()
            if (response.success && response.data != null) {
                val logs = response.data.map { it.toDomain() }
                localDao.insertLogs(logs.map { it.toEntity() })
                Result.success(logs)
            } else {
                val cached = localDao.getAllLogs().first()
                Result.success(cached.map { it.toDomain() })
            }
        } catch (e: Exception) {
            val cached = localDao.getAllLogs().first()
            if (cached.isNotEmpty()) {
                Result.success(cached.map { it.toDomain() })
            } else {
                Result.failure(e)
            }
        }
    }
}
