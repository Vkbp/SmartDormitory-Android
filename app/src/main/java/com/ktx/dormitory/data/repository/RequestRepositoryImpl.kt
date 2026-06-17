package com.ktx.dormitory.data.repository

import com.ktx.dormitory.core.network.toUserFriendlyMessage
import com.ktx.dormitory.data.api.CreateDormRequest
import com.ktx.dormitory.data.api.RequestApiService
import com.ktx.dormitory.data.api.UpdateStatusRequest
import com.ktx.dormitory.domain.model.DormRequest
import com.ktx.dormitory.domain.model.RequestStatus
import com.ktx.dormitory.domain.model.RequestType
import com.ktx.dormitory.domain.repository.RequestRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RequestRepositoryImpl @Inject constructor(
    private val api: RequestApiService
) : RequestRepository {

    override suspend fun getMyRequests(): Result<List<DormRequest>> {
        return try {
            val response = api.getMyRequests()
            if (response.success && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createRequest(type: RequestType, content: String): Result<Unit> {
        return try {
            val response = api.createRequest(CreateDormRequest(type, content))
            if (response.success) {
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(Exception(e.toUserFriendlyMessage()))
        }
    }

    override suspend fun getAllRequests(): Result<List<DormRequest>> {
        return try {
            val response = api.getAllRequests()
            if (response.success && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(Exception(e.toUserFriendlyMessage()))
        }
    }

    override suspend fun updateRequestStatus(id: String, status: RequestStatus): Result<Unit> {
        return try {
            val response = api.updateRequestStatus(id, UpdateStatusRequest(status))
            if (response.success) {
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(Exception(e.toUserFriendlyMessage()))
        }
    }
}