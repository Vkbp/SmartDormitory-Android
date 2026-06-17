package com.ktx.dormitory.domain.repository

import com.ktx.dormitory.core.utils.SecurityUtils
import com.ktx.dormitory.data.api.AccessApiService
import com.ktx.dormitory.data.api.FaceRegisterRequest
import com.ktx.dormitory.data.local.FaceDao
import com.ktx.dormitory.data.local.FaceEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FaceRepository @Inject constructor(
    private val faceDao: FaceDao,
    private val api: AccessApiService // Inject thêm API để đồng bộ lên Server
) {
    suspend fun registerFace(studentId: String, name: String, embedding: FloatArray): Result<Unit> {
        return try {
            // 1. Lưu cục bộ (đã mã hóa)
            val encrypted = SecurityUtils.encryptEmbedding(embedding)
            faceDao.insertFace(FaceEntity(studentId, name, encrypted))

            // 2. Đồng bộ lên Server (Gửi mảng Float gốc để Server xử lý)
            val response = api.registerFaceOnServer(
                FaceRegisterRequest(studentId, embedding.toList())
            )
            
            if (response.success) {
                Result.success(Unit)
            } else {
                // Nếu server lỗi nhưng local đã lưu, vẫn có thể coi là thành công 1 phần
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAllRegisteredFaces(): List<Pair<String, FloatArray>> {
        return faceDao.getAllFaces().map { entity ->
            entity.studentName to SecurityUtils.decryptEmbedding(entity.encryptedEmbedding)
        }
    }

    suspend fun deleteFace(studentId: String) {
        faceDao.deleteFace(studentId)
    }
}
