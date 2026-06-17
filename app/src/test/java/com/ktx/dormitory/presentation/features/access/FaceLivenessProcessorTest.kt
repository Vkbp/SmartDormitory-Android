package com.ktx.dormitory.presentation.features.access

import com.google.common.truth.Truth.assertThat
import com.google.mlkit.vision.face.Face
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Test

class FaceLivenessProcessorTest {

    private lateinit var processor: FaceLivenessProcessor

    @Before
    fun setup() {
        processor = FaceLivenessProcessor()
    }

    @Test
    fun `initial state is EYE_BLINK`() {
        assertThat(processor.state.value.currentStep).isEqualTo(LivenessStep.EYE_BLINK)
        assertThat(processor.state.value.progress).isEqualTo(0f)
    }

    @Test
    fun `blink detection moves to TURN_LEFT`() {
        val face = mockk<Face>()
        
        // 1. Nhắm mắt
        every { face.leftEyeOpenProbability } returns 0.1f
        every { face.rightEyeOpenProbability } returns 0.1f
        processor.process(face)
        
        // 2. Mở mắt
        every { face.leftEyeOpenProbability } returns 0.8f
        every { face.rightEyeOpenProbability } returns 0.8f
        processor.process(face)

        assertThat(processor.state.value.currentStep).isEqualTo(LivenessStep.TURN_LEFT)
        assertThat(processor.state.value.progress).isEqualTo(0.5f)
    }

    @Test
    fun `head turn left moves to TURN_RIGHT`() {
        val face = mockk<Face>()
        
        // Hoàn thành bước 1 (Blink)
        every { face.leftEyeOpenProbability } returns 0.1f
        every { face.rightEyeOpenProbability } returns 0.1f
        processor.process(face)
        every { face.leftEyeOpenProbability } returns 0.8f
        every { face.rightEyeOpenProbability } returns 0.8f
        processor.process(face)

        // 3. Quay đầu trái (> 25 độ)
        every { face.headEulerAngleY } returns 30f
        processor.process(face)
        
        // 4. Trở về giữa (< 10 độ)
        every { face.headEulerAngleY } returns 5f
        processor.process(face)

        assertThat(processor.state.value.currentStep).isEqualTo(LivenessStep.TURN_RIGHT)
    }

    @Test
    fun `smile detection completes flow`() {
        val face = mockk<Face>()
        
        // Giả lập hoàn thành các bước trước bằng cách gọi liên tục
        // (Trong thực tế ta nên tách nhỏ hoặc mock state, nhưng ở đây test flow hoàn chỉnh)
        
        // Blink
        every { face.leftEyeOpenProbability } returns 0.1f
        every { face.rightEyeOpenProbability } returns 0.1f
        processor.process(face)
        every { face.leftEyeOpenProbability } returns 0.8f
        every { face.rightEyeOpenProbability } returns 0.8f
        processor.process(face)

        // Turn Left
        every { face.headEulerAngleY } returns 30f
        processor.process(face)
        every { face.headEulerAngleY } returns 5f
        processor.process(face)

        // Turn Right
        every { face.headEulerAngleY } returns -30f
        processor.process(face)
        every { face.headEulerAngleY } returns -5f
        processor.process(face)
        
        // Smile
        every { face.smilingProbability } returns 0.9f
        processor.process(face)

        assertThat(processor.state.value.currentStep).isEqualTo(LivenessStep.COMPLETED)
        assertThat(processor.state.value.isLivenessPassed).isEqualTo(false) // Giá trị default trong code
        assertThat(processor.state.value.progress).isEqualTo(1.0f)
    }
}
