package com.ktx.dormitory.presentation.features.access

enum class LivenessStep {
    EYE_BLINK,
    TURN_LEFT,
    TURN_RIGHT,
    SMILE,
    COMPLETED
}

data class FaceLivenessUiState(
    val currentStep: LivenessStep = LivenessStep.EYE_BLINK,
    val progress: Float = 0f,
    val isQualityMet: Boolean = false,
    val qualityMessage: String = "Vui lòng nhìn thẳng vào camera",
    val isLivenessPassed: Boolean = false
)

data class FaceQualityResult(
    val isGood: Boolean,
    val message: String = "",
    val brightness: Float = 0f,
    val isBlurry: Boolean = false,
    val faceSizeOk: Boolean = false
)
