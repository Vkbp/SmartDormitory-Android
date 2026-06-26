package com.ktx.dormitory.data.access.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "access_logs")
data class AccessLogEntity(
    @PrimaryKey val id: String,
    val studentId: String?,
    val gateId: String?,
    val buildingId: String?,
    val eventTimestamp: String?,
    val decision: String?,
    val denialReason: String?,
    val method: String?
)
