package com.ktx.dormitory.core.sync

data class CreateRequestPayload(
    val type: String,
    val content: String
)

data class UpdateRequestStatusPayload(
    val requestId: String,
    val status: String
)

data class MarkNotificationReadPayload(
    val notificationId: String
)

data class RegisterFacePayload(
    val studentId: String,
    val embedding: List<Float>
)

data class VerifyQrPayload(
    val qrCode: String
)
