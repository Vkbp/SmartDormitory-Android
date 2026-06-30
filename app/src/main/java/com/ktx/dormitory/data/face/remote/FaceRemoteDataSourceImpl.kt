package com.ktx.dormitory.data.face.remote

import com.ktx.dormitory.data.access.remote.AccessApiService
import javax.inject.Inject

/**
 * Impl gửi faceImageUrl lên Backend qua AccessApiService.
 * POST /students/me/face với body: { "faceImageUrl": "..." }
 */
class FaceRemoteDataSourceImpl @Inject constructor(
    private val api: AccessApiService
) : FaceRemoteDataSource {
    override suspend fun registerFaceOnServer(studentId: String, faceImageUrl: String): String =
        api.registerFaceOnServer(studentId, hashMapOf("faceImageUrl" to faceImageUrl))
}
