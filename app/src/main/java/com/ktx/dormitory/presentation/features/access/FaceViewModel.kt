package com.ktx.dormitory.presentation.features.access

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ktx.dormitory.core.utils.DataStoreManager
import com.ktx.dormitory.domain.repository.FaceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FaceViewModel @Inject constructor(
    private val faceRepository: FaceRepository,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {
    
    private val livenessProcessor = FaceLivenessProcessor()
    val livenessState = livenessProcessor.state

    private val _qualityState = MutableStateFlow(FaceQualityResult(false))
    val qualityState = _qualityState.asStateFlow()

    private val _isRegistering = MutableStateFlow(false)
    val isRegistering = _isRegistering.asStateFlow()

    private val _registrationSuccess = MutableStateFlow(false)
    val registrationSuccess = _registrationSuccess.asStateFlow()

    private val _verificationResult = MutableStateFlow<VerificationResult?>(null)
    val verificationResult = _verificationResult.asStateFlow()

    // Cache để tối ưu tìm kiếm (Mức 1: Caching)
    private var faceCache: List<Pair<String, FloatArray>>? = null

    fun onFrameAnalyzed(face: com.google.mlkit.vision.face.Face, bitmap: Bitmap) {
        val quality = FaceQualityManager.checkQuality(face, bitmap)
        _qualityState.value = quality

        if (quality.isGood) {
            livenessProcessor.process(face)
        }
    }

    fun registerFace(studentId: String, name: String, embedding: FloatArray) {
        if (livenessState.value.currentStep != LivenessStep.COMPLETED) return

        viewModelScope.launch {
            _isRegistering.value = true
            faceRepository.registerFace(studentId, name, embedding)
            faceCache = null // Invalidate cache
            _isRegistering.value = false
            _registrationSuccess.value = true
        }
    }

    fun verifyFace(currentEmbedding: FloatArray) {
        if (livenessState.value.currentStep != LivenessStep.COMPLETED) return

        viewModelScope.launch {
            val registeredFaces = faceCache ?: faceRepository.getAllRegisteredFaces().also { faceCache = it }
            
            // Lấy Threshold từ DataStore (Configurable)
            val threshold = dataStoreManager.faceThreshold.first()

            var bestMatchName: String? = null
            var minDistance = Float.MAX_VALUE

            // Optimization: Linear search with cache is OK for < 1000 users. 
            // For > 5000, we would use a spatial index (KD-Tree)
            for (face in registeredFaces) {
                val distance = calculateDistance(currentEmbedding, face.second)
                if (distance < minDistance) {
                    minDistance = distance
                    bestMatchName = face.first
                }
            }

            if (minDistance < threshold && bestMatchName != null) {
                val confidence = ((1.0f - (minDistance / threshold)) * 100).coerceIn(0f, 100f)
                _verificationResult.value = VerificationResult(
                    isMatched = true,
                    studentName = bestMatchName,
                    confidence = confidence.toInt()
                )
            } else {
                _verificationResult.value = VerificationResult(isMatched = false)
            }
        }
    }

    private fun calculateDistance(v1: FloatArray, v2: FloatArray): Float {
        var sum = 0f
        for (i in v1.indices) {
            val diff = v1[i] - v2[i]
            sum += diff * diff
        }
        return kotlin.math.sqrt(sum)
    }

    fun resetStatus() {
        _registrationSuccess.value = false
        _verificationResult.value = null
        livenessProcessor.reset()
    }
}

data class VerificationResult(
    val isMatched: Boolean,
    val studentName: String = "",
    val confidence: Int = 0
)
