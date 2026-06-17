package com.ktx.dormitory.domain.repository

import com.ktx.dormitory.domain.model.DormRequest
import com.ktx.dormitory.domain.model.RequestStatus
import com.ktx.dormitory.domain.model.RequestType

interface RequestRepository {
    suspend fun getMyRequests(): Result<List<DormRequest>>
    suspend fun createRequest(type: RequestType, content: String): Result<Unit>
    suspend fun getAllRequests(): Result<List<DormRequest>>
    suspend fun updateRequestStatus(id: String, status: RequestStatus): Result<Unit>
}