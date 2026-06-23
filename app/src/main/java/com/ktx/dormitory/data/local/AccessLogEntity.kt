package com.ktx.dormitory.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.ktx.dormitory.domain.model.AccessLog

/**
 * AccessLogEntity - Entity lưu trữ lịch sử ra vào cục bộ.
 * Schema khớp với Backend: /api/v1/access/history/student/{id}
 */
@Entity(
    tableName = "access_logs",
    indices = [Index(value = ["studentId"]), Index(value = ["eventTimestamp"])]
)
data class AccessLogEntity(
    @PrimaryKey val id: String,
    val studentId: String?,
    val gateId: String?,
    val buildingId: String?,
    val eventTimestamp: String?,
    val decision: String?,      // GRANTED / DENIED
    val denialReason: String?,
    val method: String?         // QR / FACE / RFID
)

fun AccessLogEntity.toDomain() = AccessLog(
    id = id,
    studentId = studentId,
    gateId = gateId,
    buildingId = buildingId,
    eventTimestamp = eventTimestamp,
    decision = decision,
    denialReason = denialReason,
    method = method
)

fun AccessLog.toEntity() = AccessLogEntity(
    id = id,
    studentId = studentId,
    gateId = gateId,
    buildingId = buildingId,
    eventTimestamp = eventTimestamp,
    decision = decision,
    denialReason = denialReason,
    method = method
)
