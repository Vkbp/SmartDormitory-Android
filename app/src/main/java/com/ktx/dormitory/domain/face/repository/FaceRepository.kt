package com.ktx.dormitory.domain.face.repository

interface FaceRepository {
    suspend fun registerFace(studentId: String, name: String, embedding: FloatArray, faceImageUrl: String): Result<Unit>
    suspend fun getAllRegisteredFaces(): List<Pair<String, FloatArray>>
    suspend fun deleteFace(studentId: String)
}
