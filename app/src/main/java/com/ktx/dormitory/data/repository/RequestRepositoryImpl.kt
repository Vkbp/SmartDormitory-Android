package com.ktx.dormitory.data.repository

import com.google.gson.Gson
import com.ktx.dormitory.core.network.NetworkMonitor
import com.ktx.dormitory.core.sync.CreateRequestPayload
import com.ktx.dormitory.core.sync.SyncScheduler
import com.ktx.dormitory.core.sync.UpdateRequestStatusPayload
import com.ktx.dormitory.data.local.dao.DormRequestDao
import com.ktx.dormitory.data.local.dao.PendingSyncDao
import com.ktx.dormitory.data.local.entity.PendingSyncEntity
import com.ktx.dormitory.data.remote.api.RequestApiService
import com.ktx.dormitory.data.mapper.*
import com.ktx.dormitory.data.remote.dto.common.DormRequestDto
import com.ktx.dormitory.domain.model.DormRequest
import com.ktx.dormitory.domain.model.RequestStatus
import com.ktx.dormitory.domain.model.RequestType
import com.ktx.dormitory.domain.repository.RequestRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RequestRepositoryImpl @Inject constructor(
    private val api: RequestApiService,
    private val localDao: DormRequestDao,
    private val pendingSyncDao: PendingSyncDao,
    private val syncScheduler: SyncScheduler,
    private val networkMonitor: NetworkMonitor
) : RequestRepository {

    private val gson = Gson()

    override suspend fun submitRequest(type: RequestType, content: String): Result<Unit> {
        val payload = gson.toJson(CreateRequestPayload(type.name, content))
        return try {
            val isOnline = networkMonitor.isOnline.first()
            if (!isOnline) {
                return queueAction("CREATE_REQUEST", payload)
            }

            val dto = DormRequestDto(
                id = "",
                studentName = null,
                studentId = null,
                type = type.name,
                content = content,
                status = "PENDING",
                createdAt = null
            )
            val response = api.submitRequest(dto)
            if (response.success) Result.success(Unit) else Result.failure(Exception(response.message))
        } catch (e: Exception) {
            if (isNetworkException(e)) {
                queueAction("CREATE_REQUEST", payload)
            } else {
                Result.failure(e)
            }
        }
    }

    override suspend fun getMyRequests(): Result<List<DormRequest>> {
        return try {
            val response = api.getMyRequests()
            if (response.success && response.data != null) {
                val requests = response.data.map { it.toDomain() }
                localDao.insertRequests(response.data.map { it.toEntity() })
                Result.success(requests)
            } else {
                val cached = localDao.getAllRequests().first()
                Result.success(cached.map { it.toDomain() })
            }
        } catch (e: Exception) {
            val cached = localDao.getAllRequests().first()
            if (cached.isNotEmpty()) {
                Result.success(cached.map { it.toDomain() })
            } else {
                Result.failure(e)
            }
        }
    }

    override suspend fun getPendingRequests(): Result<List<DormRequest>> {
        return try {
            val response = api.getPendingRequests()
            if (response.success && response.data != null) {
                Result.success(response.data.map { it.toDomain() })
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateRequestStatus(requestId: String, status: RequestStatus): Result<Unit> {
        val payload = gson.toJson(UpdateRequestStatusPayload(requestId, status.name))
        return try {
            val isOnline = networkMonitor.isOnline.first()
            if (!isOnline) {
                return queueAction("UPDATE_REQUEST_STATUS", payload)
            }
            val response = api.updateRequestStatus(requestId, status.name)
            if (response.success) Result.success(Unit) else Result.failure(Exception(response.message))
        } catch (e: Exception) {
            if (isNetworkException(e)) {
                queueAction("UPDATE_REQUEST_STATUS", payload)
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
}
