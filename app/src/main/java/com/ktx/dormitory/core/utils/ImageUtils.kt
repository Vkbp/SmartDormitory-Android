package com.ktx.dormitory.core.utils

import android.graphics.*
import androidx.camera.core.ImageProxy

/**
 * Chuyển đổi ImageProxy sang Bitmap và xoay đúng hướng.
 * Đổi tên thành toBitmapRotation để tránh xung đột với thư viện CameraX 1.3.0+
 */
fun ImageProxy.toBitmapRotation(): Bitmap? {
    val bitmap = this.toBitmap() // Gọi hàm toBitmap() mặc định của CameraX
    
    // Xoay ảnh cho đúng hướng dựa trên ImageInfo
    val matrix = Matrix()
    matrix.postRotate(this.imageInfo.rotationDegrees.toFloat())
    
    return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
}

/**
 * Cắt khuôn mặt từ ảnh gốc dựa trên Bounding Box của ML Kit
 */
fun Bitmap.cropFace(boundingBox: Rect): Bitmap {
    val left = boundingBox.left.coerceAtLeast(0)
    val top = boundingBox.top.coerceAtLeast(0)
    val width = boundingBox.width().coerceAtMost(this.width - left)
    val height = boundingBox.height().coerceAtMost(this.height - top)
    
    return Bitmap.createBitmap(this, left, top, width, height)
}
