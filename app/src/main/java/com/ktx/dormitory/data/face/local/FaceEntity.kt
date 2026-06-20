package com.ktx.dormitory.data.face.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "registered_faces")
data class FaceEntity(
    @PrimaryKey val studentId: String,
    val studentName: String,
    val encryptedEmbedding: ByteArray,
    val createdAt: Long = System.currentTimeMillis()
)
