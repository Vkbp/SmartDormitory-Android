package com.ktx.dormitory.domain.model

import com.google.gson.annotations.SerializedName

enum class RequestType(val displayName: String) {
    REPAIR("Sửa chữa"),
    COMPLAINT("Khiếu nại"),
    EXTEND("Gia hạn lưu trú"),
    OTHER("Khác")
}

enum class RequestStatus { PENDING, APPROVED, REJECTED }

data class DormRequest(
    val id: String,
    val studentName: String?,
    val studentId: String?,
    val type: RequestType?,
    val content: String?,
    val status: RequestStatus,
    val createdAt: String?
)
