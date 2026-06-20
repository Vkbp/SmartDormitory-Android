package com.ktx.dormitory.domain.face.usecase

import com.ktx.dormitory.domain.face.repository.FaceRepository
import javax.inject.Inject

class RegisterFaceUseCase @Inject constructor(
    private val faceRepository: FaceRepository
) {
    suspend operator fun invoke(studentId: String, name: String, embedding: FloatArray): Result<Unit> {
        return faceRepository.registerFace(studentId, name, embedding)
    }
}
