package com.ktx.dormitory.domain.model

data class AccessLog(
    val id: String,
    val userId: String?,
    val location: String?,
    val timestamp: String?,
    val isSuccess: Boolean,
    val method: String? // QR Code hoặc RFID
)