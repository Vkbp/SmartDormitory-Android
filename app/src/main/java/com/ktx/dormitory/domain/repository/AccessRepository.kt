package com.ktx.dormitory.domain.repository

import com.ktx.dormitory.domain.model.AccessLog
import kotlinx.coroutines.flow.Flow

interface AccessRepository {
    val accessLogs: Flow<List<AccessLog>>
    suspend fun registerFaceOnServer(studentId: String, faceImageUrl: String): Result<Unit>
    suspend fun getAccessHistory(studentId: String): Result<List<AccessLog>>
}
