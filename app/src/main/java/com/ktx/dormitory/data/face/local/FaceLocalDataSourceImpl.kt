package com.ktx.dormitory.data.face.local

import javax.inject.Inject

class FaceLocalDataSourceImpl @Inject constructor(
    private val faceDao: FaceDao
) : FaceLocalDataSource {
    override suspend fun insertFace(face: FaceEntity) = faceDao.insertFace(face)
    override suspend fun getAllFaces(): List<FaceEntity> = faceDao.getAllFaces()
    override suspend fun deleteFace(studentId: String) = faceDao.deleteFace(studentId)
}
