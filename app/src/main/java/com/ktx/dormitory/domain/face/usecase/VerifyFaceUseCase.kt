package com.ktx.dormitory.domain.face.usecase

import com.ktx.dormitory.domain.face.model.FaceVerificationResult
import com.ktx.dormitory.domain.face.repository.FaceRepository
import javax.inject.Inject
import kotlin.math.sqrt

class VerifyFaceUseCase @Inject constructor(
    private val faceRepository: FaceRepository
) {
    suspend operator fun invoke(currentEmbedding: FloatArray, threshold: Float): FaceVerificationResult {
        val registeredFaces = faceRepository.getAllRegisteredFaces()
        if (registeredFaces.isEmpty()) {
            return FaceVerificationResult(isMatched = false)
        }

        var bestMatchName: String? = null
        var minDistance = Float.MAX_VALUE

        for (face in registeredFaces) {
            val distance = calculateDistance(currentEmbedding, face.second)
            if (distance < minDistance) {
                minDistance = distance
                bestMatchName = face.first
            }
        }

        return if (minDistance < threshold && bestMatchName != null) {
            val confidence = ((1.0f - (minDistance / threshold)) * 100).coerceIn(0f, 100f)
            FaceVerificationResult(
                isMatched = true,
                studentName = bestMatchName,
                confidence = confidence.toInt()
            )
        } else {
            FaceVerificationResult(isMatched = false)
        }
    }

    private fun calculateDistance(v1: FloatArray, v2: FloatArray): Float {
        var sum = 0f
        for (i in v1.indices) {
            val diff = v1[i] - v2[i]
            sum += diff * diff
        }
        return sqrt(sum)
    }
}
