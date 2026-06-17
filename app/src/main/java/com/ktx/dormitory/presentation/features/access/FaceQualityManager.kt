package com.ktx.dormitory.presentation.features.access

import android.graphics.Bitmap
import android.graphics.Color
import com.google.mlkit.vision.face.Face
import kotlin.math.abs

object FaceQualityManager {

    private const val MIN_BRIGHTNESS = 40f
    private const val MAX_BRIGHTNESS = 230f
    private const val MIN_FACE_SIZE_PERCENT = 0.25f // Mặt phải chiếm ít nhất 25% khung hình
    private const val MAX_HEAD_ANGLE = 15f // Góc quay đầu tối đa khi đăng ký (nhìn thẳng)

    fun checkQuality(face: Face, bitmap: Bitmap): FaceQualityResult {
        // 1. Kiểm tra kích thước khuôn mặt
        val faceArea = face.boundingBox.width() * face.boundingBox.height()
        val imageArea = bitmap.width * bitmap.height
        val faceSizeRatio = faceArea.toFloat() / imageArea
        
        if (faceSizeRatio < MIN_FACE_SIZE_PERCENT) {
            return FaceQualityResult(false, "Vui lòng đưa mặt lại gần hơn", faceSizeOk = false)
        }

        // 2. Kiểm tra góc quay đầu (Nhìn thẳng)
        if (abs(face.headEulerAngleY) > MAX_HEAD_ANGLE || abs(face.headEulerAngleZ) > MAX_HEAD_ANGLE) {
            return FaceQualityResult(false, "Vui lòng nhìn thẳng vào camera")
        }

        // 3. Kiểm tra độ sáng (Simple average)
        val brightness = calculateBrightness(bitmap, face.boundingBox)
        if (brightness < MIN_BRIGHTNESS) {
            return FaceQualityResult(false, "Ánh sáng quá tối", brightness = brightness)
        }
        if (brightness > MAX_BRIGHTNESS) {
            return FaceQualityResult(false, "Ánh sáng quá chói", brightness = brightness)
        }

        // 4. Kiểm tra nhắm mắt (Chỉ dùng cho đăng ký)
        val leftEyeOpen = face.leftEyeOpenProbability ?: 1.0f
        val rightEyeOpen = face.rightEyeOpenProbability ?: 1.0f
        if (leftEyeOpen < 0.4f || rightEyeOpen < 0.4f) {
            return FaceQualityResult(false, "Vui lòng mở mắt")
        }

        return FaceQualityResult(true, "Chất lượng đạt chuẩn", brightness = brightness, faceSizeOk = true)
    }

    private fun calculateBrightness(bitmap: Bitmap, rect: android.graphics.Rect): Float {
        var totalLuminance = 0L
        var count = 0
        
        val left = rect.left.coerceAtLeast(0)
        val top = rect.top.coerceAtLeast(0)
        val right = rect.right.coerceAtMost(bitmap.width - 1)
        val bottom = rect.bottom.coerceAtMost(bitmap.height - 1)

        // Lấy mẫu thưa để tiết kiệm CPU
        for (y in top until bottom step 10) {
            for (x in left until right step 10) {
                val pixel = bitmap.getPixel(x, y)
                val r = Color.red(pixel)
                val g = Color.green(pixel)
                val b = Color.blue(pixel)
                // Công thức tính Luminance chuẩn (ITU-R BT.601)
                totalLuminance += (0.299 * r + 0.587 * g + 0.114 * b).toLong()
                count++
            }
        }
        return if (count > 0) totalLuminance.toFloat() / count else 0f
    }
}
