package com.ktx.dormitory.data.repository

import com.ktx.dormitory.data.local.dao.AccessLogDao
import com.ktx.dormitory.data.local.toDomain
import com.ktx.dormitory.data.local.toEntity
import com.ktx.dormitory.data.remote.datasource.AccessRemoteDataSource
import com.ktx.dormitory.data.mapper.toDomain
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
    private val localDao: AccessLogDao
) : AccessRepository {

    override val accessLogs: Flow<List<AccessLog>> = localDao.getAllLogs()
        .map { list -> list.map { it.toDomain() } }

    /**
     * Gửi URL ảnh khuôn mặt (đã upload) lên server để đăng ký Face ID.
     * Flow: Upload ảnh → nhận URL → gọi hàm này với URL.
     * @param studentId UUID của sinh viên (lấy từ profile/auth)
     * @param faceImageUrl URL ảnh khuôn mặt
     */
    override suspend fun registerFaceOnServer(studentId: String, faceImageUrl: String): Result<Unit> {
        return try {
            val faceId = remoteDataSource.registerFaceOnServer(studentId, faceImageUrl)
            if (faceId.isNotBlank()) Result.success(Unit)
            else Result.failure(Exception("Face registration failed: No face ID returned"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Lấy lịch sử ra vào theo studentId.
     * Backend trả về Page<AccessHistory> (không bọc BaseResponse).
     */
    override suspend fun getAccessHistory(studentId: String): Result<List<AccessLog>> {
        return try {
            val page = remoteDataSource.getAccessHistory(studentId)
            val logs = page.content.map { it.toDomain() }
            localDao.insertLogs(logs.map { it.toEntity() })
            Result.success(logs)
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
