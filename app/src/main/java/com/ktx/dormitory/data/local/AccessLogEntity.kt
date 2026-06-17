package com.ktx.dormitory.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ktx.dormitory.domain.model.AccessLog

@Entity(tableName = "access_logs")
data class AccessLogEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val location: String,
    val timestamp: String,
    val isSuccess: Boolean,
    val method: String
)

fun AccessLogEntity.toDomain() = AccessLog(
    id = id,
    userId = userId,
    location = location,
    timestamp = timestamp,
    isSuccess = isSuccess,
    method = method
)

fun AccessLog.toEntity() = AccessLogEntity(
    id = id,
    userId = userId ?: "",
    location = location ?: "",
    timestamp = timestamp ?: "",
    isSuccess = isSuccess,
    method = method ?: ""
)
