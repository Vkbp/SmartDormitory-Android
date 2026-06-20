package com.ktx.dormitory.domain.repository

import com.ktx.dormitory.domain.model.AccessLog
import kotlinx.coroutines.flow.Flow

interface AccessRepository {
    val accessLogs: Flow<List<AccessLog>>
    suspend fun verifyQrCode(qrCode: String): Result<Unit>
    suspend fun registerFaceOnServer(studentId: String, embedding: List<Float>): Result<Unit>
    suspend fun getAccessHistory(): Result<List<AccessLog>>
}
