package com.ktx.dormitory.data.face.remote

/**
 * Interface datasource cho Face Registration.
 * Backend Flow: Upload ảnh → nhận URL → gửi URL qua POST /students/me/face
 * Không gửi embedding! Backend tự xử lý embedding từ ảnh.
 */
interface FaceRemoteDataSource {
    suspend fun registerFaceOnServer(studentId: String, faceImageUrl: String): String
}
