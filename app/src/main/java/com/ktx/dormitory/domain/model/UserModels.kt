package com.ktx.dormitory.domain.model

import com.google.gson.annotations.SerializedName

/**
 * 1. Hồ sơ người dùng chi tiết (Khớp Spec 4 - Profile)
 */
data class UserProfile(
    @SerializedName("id") val id: Long? = null,
    @SerializedName("username") val username: String? = null,
    @SerializedName("studentCode", alternate = ["student_code"]) val studentCode: String? = null,
    @SerializedName("fullName", alternate = ["full_name"]) val fullName: String? = null,
    @SerializedName("cccd") val cccd: String? = null,
    @SerializedName("gender") val gender: String? = null,
    @SerializedName("birthDate", alternate = ["date_of_birth", "birth_date"]) val birthDate: String? = null,
    @SerializedName("faculty") val faculty: String? = null,
    @SerializedName("course") val course: String? = null,
    @SerializedName("phone") val phone: String? = null,
    @SerializedName("email") val email: String? = null,
    @SerializedName("address") val address: String? = null,
    @SerializedName("role") val role: String? = null,
    @SerializedName("avatarUrl", alternate = ["avatar_url"]) val avatarUrl: String? = null
)

/**
 * 2. Thông tin Phòng
 */
data class RoomInfo(
    @SerializedName("building") val building: String?,
    @SerializedName("floor") val floor: String?,
    @SerializedName("room_code") val roomCode: String?,
    @SerializedName("bed_code") val bedCode: String?,
    @SerializedName("status") val status: String?
)

/**
 * 3. Giao dịch thanh toán (Khớp Spec 3 - Payments)
 */
data class Transaction(
    @SerializedName("transaction_id") val transactionId: String?,
    @SerializedName("amount") val amount: Double?,
    @SerializedName("payment_method") val method: String?, // Khớp payment_method
    @SerializedName("status") val status: String?,
    @SerializedName("created_at") val createdAt: String?, // Khớp created_at ISO
    @SerializedName("type") val type: String? = null,
    @SerializedName("message") val message: String? = null
)

data class DormApplication(
    @SerializedName("application_code") val applicationCode: String?,
    @SerializedName("status") val status: String?,
    @SerializedName("submission_date") val submissionDate: String?,
    @SerializedName("payment_deadline") val paymentDeadline: String?,
    @SerializedName("timeline") val timeline: List<TimelineStep> = emptyList()
)

data class TimelineStep(
    @SerializedName("title") val title: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("timestamp") val timestamp: String?,
    @SerializedName("step_status") val stepStatus: AppStepStatus?
)

enum class AppStepStatus { 
    @SerializedName("COMPLETED") COMPLETED, 
    @SerializedName("CURRENT") CURRENT, 
    @SerializedName("WAITING") WAITING 
}

/**
 * 4. Yêu cầu cập nhật hồ sơ
 */
data class UpdateProfileRequest(
    @SerializedName("fullName", alternate = ["full_name"]) val fullName: String?,
    @SerializedName("phone") val phone: String?,
    @SerializedName("email") val email: String?,
    @SerializedName("address") val address: String? = null
)
