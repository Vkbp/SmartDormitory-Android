package com.ktx.dormitory.data.access.repository

import com.ktx.dormitory.data.access.mapper.toDomain
import com.ktx.dormitory.data.access.mapper.toEntity
import com.ktx.dormitory.data.access.remote.AccessRemoteDataSource
import com.ktx.dormitory.data.local.dao.AccessLogDao
import com.ktx.dormitory.data.face.remote.FaceRemoteDataSource
import com.ktx.dormitory.domain.access.model.AccessLog
import com.ktx.dormitory.domain.access.repository.AccessRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccessRepositoryImpl @Inject constructor(
    private val remoteDataSource: AccessRemoteDataSource,
    private val faceRemoteDataSource: FaceRemoteDataSource,
    private val dao: AccessLogDao
) : AccessRepository {

    override val accessLogs: Flow<List<AccessLog>> = dao.getAllLogs()
        .map { list -> list.map { it.toDomain() } }

    override suspend fun registerFaceOnServer(studentId: String, faceImageUrl: String): Result<Unit> {
        return try {
            faceRemoteDataSource.registerFaceOnServer(studentId, faceImageUrl)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getAccessHistory(studentId: String): Result<List<AccessLog>> {
        return try {
            val page = remoteDataSource.getAccessHistory(studentId)
            val logs = page.content.map { it.toDomain() }
            dao.insertLogs(page.content.map { it.toEntity() })
            Result.success(logs)
        } catch (e: Exception) {
            val cached = dao.getAllLogs().first()
            if (cached.isNotEmpty()) {
                Result.success(cached.map { it.toDomain() })
            } else {
                Result.failure(e)
            }
        }
    }
}
