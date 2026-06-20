package com.ktx.dormitory.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class RequestType(val displayName: String) : Parcelable {
    REPAIR("Sửa chữa"),
    COMPLAINT("Khiếu nại"),
    EXTEND("Gia hạn lưu trú"),
    OTHER("Khác")
}

@Parcelize
enum class RequestStatus : Parcelable { PENDING, APPROVED, REJECTED }

@Parcelize
data class DormRequest(
    val id: String,
    val studentName: String?,
    val studentId: String?,
    val type: RequestType?,
    val content: String?,
    val status: RequestStatus,
    val createdAt: String?
) : Parcelable
