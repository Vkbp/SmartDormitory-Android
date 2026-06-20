package com.ktx.dormitory.domain.repository

import com.ktx.dormitory.domain.model.DormRequest
import com.ktx.dormitory.domain.model.RequestStatus
import com.ktx.dormitory.domain.model.RequestType

interface RequestRepository {
    suspend fun submitRequest(type: RequestType, content: String): Result<Unit>
    suspend fun getMyRequests(): Result<List<DormRequest>>
    suspend fun getPendingRequests(): Result<List<DormRequest>>
    suspend fun updateRequestStatus(requestId: String, status: RequestStatus): Result<Unit>
}
