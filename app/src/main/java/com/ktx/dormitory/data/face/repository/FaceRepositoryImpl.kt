package com.ktx.dormitory.data.face.repository

import com.ktx.dormitory.core.utils.SecurityUtils
import com.ktx.dormitory.data.face.local.FaceEntity
import com.ktx.dormitory.data.face.local.FaceLocalDataSource
import com.ktx.dormitory.data.face.remote.FaceRemoteDataSource
import com.ktx.dormitory.domain.face.repository.FaceRepository
import javax.inject.Inject
import javax.inject.Singleton

/**
 * FaceRepositoryImpl - Quản lý đăng ký và xác thực khuôn mặt.
 *
 * Backend Flow:
 * 1. Client upload ảnh → nhận URL (qua UserApiService.uploadAvatar)
 * 2. Gọi registerFace với faceImageUrl
 * 3. Server xử lý embedding từ ảnh (Backend tự làm)
 *
 * Local: Lưu embedding mã hóa trong Room để xác thực offline (Offline AI).
 * Remote: Gửi URL ảnh lên Backend qua POST /students/me/face.
 *
 * NOTE: Không gửi embedding lên Backend. Backend không có API nhận embedding.
 */
@Singleton
class FaceRepositoryImpl @Inject constructor(
    private val localDataSource: FaceLocalDataSource,
    private val remoteDataSource: FaceRemoteDataSource
) : FaceRepository {

    /**
     * Đăng ký khuôn mặt.
     * @param studentId UUID sinh viên
     * @param name Tên sinh viên (để hiển thị)
     * @param embedding Vector đặc trưng khuôn mặt (chỉ lưu LOCAL, không gửi lên Backend)
     * @param faceImageUrl URL ảnh khuôn mặt (gửi lên Backend thay vì embedding)
     */
    override suspend fun registerFace(
        studentId: String,
        name: String,
        embedding: FloatArray,
        faceImageUrl: String
    ): Result<Unit> {
        return try {
            // 1. Mã hóa và lưu embedding cục bộ để xác thực offline
            val encrypted = SecurityUtils.encryptEmbedding(embedding)
            localDataSource.insertFace(FaceEntity(studentId, name, encrypted))

            // 2. Gửi URL ảnh lên Backend (KHÔNG gửi embedding)
            val response = remoteDataSource.registerFaceOnServer(studentId, faceImageUrl)
            if (response.isNotBlank()) Result.success(Unit)
            else Result.failure(Exception("Đăng ký khuôn mặt thất bại"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getAllRegisteredFaces(): List<Pair<String, FloatArray>> {
        return localDataSource.getAllFaces().map { entity ->
            entity.studentName to SecurityUtils.decryptEmbedding(entity.encryptedEmbedding)
        }
    }

    override suspend fun deleteFace(studentId: String) {
        localDataSource.deleteFace(studentId)
    }
}
