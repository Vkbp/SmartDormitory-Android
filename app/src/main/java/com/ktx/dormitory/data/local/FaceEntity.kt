package com.ktx.dormitory.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "registered_faces")
data class FaceEntity(
    @PrimaryKey val studentId: String,
    val studentName: String,
    val encryptedEmbedding: ByteArray, // Đổi từ FloatArray sang ByteArray để lưu dữ liệu đã mã hóa
    val createdAt: Long = System.currentTimeMillis()
)
