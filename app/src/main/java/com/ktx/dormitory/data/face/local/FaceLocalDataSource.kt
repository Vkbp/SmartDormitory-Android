package com.ktx.dormitory.data.face.local

interface FaceLocalDataSource {
    suspend fun insertFace(face: FaceEntity)
    suspend fun getAllFaces(): List<FaceEntity>
    suspend fun deleteFace(studentId: String)
}
