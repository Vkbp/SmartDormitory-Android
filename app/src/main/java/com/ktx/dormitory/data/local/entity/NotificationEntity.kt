package com.ktx.dormitory.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey val id: String,
    val title: String?,
    val message: String?,
    val time: String?,
    val isRead: Boolean
)
