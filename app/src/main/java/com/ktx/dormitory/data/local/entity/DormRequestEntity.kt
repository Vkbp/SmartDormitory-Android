package com.ktx.dormitory.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dorm_requests")
data class DormRequestEntity(
    @PrimaryKey val id: String,
    val studentName: String?,
    val studentId: String?,
    val type: String?,
    val content: String?,
    val status: String,
    val createdAt: String?
)
