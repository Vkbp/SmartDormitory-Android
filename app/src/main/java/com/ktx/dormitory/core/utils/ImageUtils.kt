package com.ktx.dormitory.core.utils

import android.graphics.*
import androidx.camera.core.ImageProxy

/**
 * Chuyển đổi ImageProxy sang Bitmap và xoay đúng hướng.
 * Tối ưu hóa bộ nhớ bằng cách recycle bitmap trung gian.
 */
fun ImageProxy.toBitmapRotation(): Bitmap? {
    val bitmap = this.toBitmap() ?: return null
    
    return if (this.imageInfo.rotationDegrees != 0) {
        val matrix = Matrix()
        matrix.postRotate(this.imageInfo.rotationDegrees.toFloat())
        val rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        if (rotatedBitmap != bitmap) {
            bitmap.recycle() // Giải phóng bitmap cũ ngay lập tức
        }
        rotatedBitmap
    } else {
        bitmap
    }
}

/**
 * Cắt khuôn mặt từ ảnh gốc dựa trên Bounding Box của ML Kit
 */
fun Bitmap.cropFace(boundingBox: Rect): Bitmap {
    val left = boundingBox.left.coerceAtLeast(0)
    val top = boundingBox.top.coerceAtLeast(0)
    val width = boundingBox.width().coerceAtMost(this.width - left)
    val height = boundingBox.height().coerceAtMost(this.height - top)
    
    // Nếu kích thước không hợp lệ, trả về chính nó hoặc xử lý lỗi
    if (width <= 0 || height <= 0) return this
    
    return Bitmap.createBitmap(this, left, top, width, height)
}

/**
 * Lưu Bitmap vào tệp cache
 */
fun Bitmap.saveToFile(context: android.content.Context, fileName: String): String? {
    return try {
        val file = java.io.File(context.cacheDir, fileName)
        java.io.FileOutputStream(file).use { out ->
            this.compress(Bitmap.CompressFormat.JPEG, 90, out)
        }
        file.absolutePath
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
