package com.ktx.dormitory.domain.model

data class AccessLog(
    val id: String,
    val studentId: String?,
    val gateId: String?,
    val buildingId: String?,
    val eventTimestamp: String?,
    val decision: String?,   // GRANTED / DENIED
    val denialReason: String?,
    val method: String?      // QR / FACE / RFID
)