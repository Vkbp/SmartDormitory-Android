package com.ktx.dormitory.data.face.repository

import com.ktx.dormitory.core.utils.SecurityUtils
import com.ktx.dormitory.data.face.local.FaceEntity
import com.ktx.dormitory.data.face.local.FaceLocalDataSource
import com.ktx.dormitory.data.face.remote.FaceRemoteDataSource
import com.ktx.dormitory.data.remote.dto.common.FaceRegisterRequestDto
import com.ktx.dormitory.domain.face.repository.FaceRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FaceRepositoryImpl @Inject constructor(
    private val localDataSource: FaceLocalDataSource,
    private val remoteDataSource: FaceRemoteDataSource
) : FaceRepository {

    override suspend fun registerFace(studentId: String, name: String, embedding: FloatArray): Result<Unit> {
        return try {
            val encrypted = SecurityUtils.encryptEmbedding(embedding)
            localDataSource.insertFace(FaceEntity(studentId, name, encrypted))
            
            val response = remoteDataSource.registerFaceOnServer(FaceRegisterRequestDto(studentId, embedding.toList()))
            if (response.success) Result.success(Unit) else Result.failure(Exception(response.message))
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
