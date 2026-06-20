package com.ktx.dormitory.presentation.face.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ktx.dormitory.ai.processing.*
import com.ktx.dormitory.domain.repository.SettingsRepository
import com.ktx.dormitory.domain.face.model.FaceVerificationResult
import com.ktx.dormitory.domain.face.usecase.RegisterFaceUseCase
import com.ktx.dormitory.domain.face.usecase.VerifyFaceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FaceViewModel @Inject constructor(
    private val registerFaceUseCase: RegisterFaceUseCase,
    private val verifyFaceUseCase: VerifyFaceUseCase,
    private val settingsRepository: SettingsRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    
    private val livenessProcessor = FaceLivenessProcessor(
        savedStateHandle.get<FaceLivenessUiState>("livenessState") ?: FaceLivenessUiState()
    )
    val livenessState = livenessProcessor.state

    val qualityState: StateFlow<FaceQualityResult> = savedStateHandle.getStateFlow("qualityState", FaceQualityResult(false))

    val isRegistering: StateFlow<Boolean> = savedStateHandle.getStateFlow("isRegistering", false)

    val registrationSuccess: StateFlow<Boolean> = savedStateHandle.getStateFlow("registrationSuccess", false)

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

    fun registerFace(studentId: String, name: String, embedding: FloatArray) {
        if (livenessState.value.currentStep != LivenessStep.COMPLETED) return
        viewModelScope.launch {
            savedStateHandle["isRegistering"] = true
            registerFaceUseCase(studentId, name, embedding)
            savedStateHandle["isRegistering"] = false
            savedStateHandle["registrationSuccess"] = true
        }
    }

    fun verifyFace(currentEmbedding: FloatArray) {
        viewModelScope.launch {
            val threshold = settingsRepository.getFaceThreshold().first()
            val result = verifyFaceUseCase(currentEmbedding, threshold)
            savedStateHandle["verificationResult"] = result
        }
    }

    fun resetStatus() {
        savedStateHandle["registrationSuccess"] = false
        savedStateHandle["verificationResult"] = null
        livenessProcessor.reset()
    }
}
