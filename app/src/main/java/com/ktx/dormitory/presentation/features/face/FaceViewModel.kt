package com.ktx.dormitory.presentation.features.face

import android.graphics.Bitmap
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ktx.dormitory.ai.processing.liveness.*
import com.ktx.dormitory.ai.processing.quality.*
import com.ktx.dormitory.data.profile.local.ProfileLocalDataSource
import com.ktx.dormitory.domain.face.model.FaceVerificationResult
import com.ktx.dormitory.domain.face.usecase.RegisterFaceUseCase
import com.ktx.dormitory.domain.face.usecase.VerifyFaceUseCase
import com.ktx.dormitory.domain.profile.usecase.UploadAvatarUseCase
import com.ktx.dormitory.domain.settings.usecase.GetFaceThresholdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FaceViewModel @Inject constructor(
    private val registerFaceUseCase: RegisterFaceUseCase,
    private val verifyFaceUseCase: VerifyFaceUseCase,
    private val uploadAvatarUseCase: UploadAvatarUseCase,
    private val getFaceThresholdUseCase: GetFaceThresholdUseCase,
    private val profileLocalDataSource: ProfileLocalDataSource,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    
    private val livenessProcessor = FaceLivenessProcessor(
        savedStateHandle.get<FaceLivenessUiState>("livenessState") ?: FaceLivenessUiState()
    )
    val livenessState = livenessProcessor.state

    val qualityState: StateFlow<FaceQualityResult> = savedStateHandle.getStateFlow("qualityState", FaceQualityResult(false))

    val isRegistering: StateFlow<Boolean> = savedStateHandle.getStateFlow("isRegistering", false)

    val registrationSuccess: StateFlow<Boolean> = savedStateHandle.getStateFlow("registrationSuccess", false)

    val errorMessage: StateFlow<String?> = savedStateHandle.getStateFlow("errorMessage", null)

    val verificationResult: StateFlow<FaceVerificationResult?> = savedStateHandle.getStateFlow("verificationResult", null)

    init {
        viewModelScope.launch {
            livenessState.collect {
                savedStateHandle["livenessState"] = it
            }
        }
    }

    fun onFrameAnalyzed(face: com.google.mlkit.vision.face.Face, bitmap: Bitmap) {
        val currentStep = livenessState.value.currentStep
        val isBlinkingStep = currentStep == LivenessStep.EYE_BLINK
        val isTurningStep = currentStep == LivenessStep.TURN_LEFT || currentStep == LivenessStep.TURN_RIGHT
        
        val quality = FaceQualityManager.checkQuality(face, bitmap, isBlinkingStep, isTurningStep)
        savedStateHandle["qualityState"] = quality
        
        val shouldProcess = quality.isGood || 
                (isBlinkingStep && quality.message == "Vui lòng mở mắt") ||
                (isTurningStep && quality.message == "Vui lòng nhìn thẳng vào camera")

        if (shouldProcess) {
            livenessProcessor.process(face)
        }
    }

    fun registerFace(name: String, embedding: FloatArray, imagePath: String) {
        if (livenessState.value.currentStep != LivenessStep.COMPLETED) return
        viewModelScope.launch {
            savedStateHandle["isRegistering"] = true
            savedStateHandle["errorMessage"] = null
            
            val profile = profileLocalDataSource.getProfile().firstOrNull()
            val studentId = profile?.id
            
            if (studentId == null) {
                savedStateHandle["isRegistering"] = false
                savedStateHandle["errorMessage"] = "Không tìm thấy thông tin sinh viên"
                return@launch
            }

            val uploadResult = uploadAvatarUseCase(imagePath)
            uploadResult.onSuccess { url ->
                val registerResult = registerFaceUseCase(studentId, name, embedding, url)
                registerResult.onSuccess {
                    savedStateHandle["isRegistering"] = false
                    savedStateHandle["registrationSuccess"] = true
                }.onFailure { e ->
                    savedStateHandle["isRegistering"] = false
                    savedStateHandle["errorMessage"] = "Đăng ký khuôn mặt thất bại: ${e.message}"
                }
            }.onFailure { e ->
                savedStateHandle["isRegistering"] = false
                savedStateHandle["errorMessage"] = "Tải ảnh lên thất bại: ${e.message}"
            }
        }
    }

    fun verifyFace(currentEmbedding: FloatArray) {
        viewModelScope.launch {
            val threshold = getFaceThresholdUseCase().first()
            val result = verifyFaceUseCase(currentEmbedding, threshold)
            savedStateHandle["verificationResult"] = result
        }
    }

    fun resetStatus() {
        savedStateHandle["registrationSuccess"] = false
        savedStateHandle["errorMessage"] = null
        savedStateHandle["verificationResult"] = null
        livenessProcessor.reset()
    }

    fun clearError() {
        savedStateHandle["errorMessage"] = null
    }
}
