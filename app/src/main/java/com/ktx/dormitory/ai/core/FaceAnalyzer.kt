package com.ktx.dormitory.ai.core

import android.annotation.SuppressLint
import android.graphics.Bitmap
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.ktx.dormitory.core.utils.toBitmapRotation
import com.ktx.dormitory.presentation.features.face.FaceViewModel

class FaceAnalyzer(
    private val viewModel: FaceViewModel,
    private val onFaceDetected: (List<Face>, Int, Int, Bitmap?) -> Unit
) : ImageAnalysis.Analyzer {

    private val options = FaceDetectorOptions.Builder()
        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
        .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
        .build()

    private val detector = FaceDetection.getClient(options)

    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            
            // Không tạo Bitmap ở đây để tiết kiệm CPU/RAM cho mỗi frame
            detector.process(image)
                .addOnSuccessListener { faces ->
                    val face = faces.firstOrNull()
                    if (face != null) {
                        // Chỉ tạo Bitmap khi thực sự có khuôn mặt để phân tích chất lượng
                        val fullBitmap = imageProxy.toBitmapRotation()
                        if (fullBitmap != null) {
                            viewModel.onFrameAnalyzed(face, fullBitmap)
                            onFaceDetected(faces, image.width, image.height, fullBitmap)
                        } else {
                            onFaceDetected(faces, image.width, image.height, null)
                        }
                    } else {
                        onFaceDetected(faces, image.width, image.height, null)
                    }
                }
                .addOnCompleteListener {
                    imageProxy.close()
                }
        } else {
            imageProxy.close()
        }
    }
}
