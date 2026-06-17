package com.ktx.dormitory.domain.model

import com.google.gson.annotations.SerializedName

/**
 * 1. Hồ sơ người dùng chi tiết (Đã bổ sung đầy đủ các trường theo API_DOCUMENTATION.md)
 */
data class UserProfile(
    @SerializedName("studentId") val id: String? = null,
    @SerializedName("studentCode") val studentCode: String? = null,
    @SerializedName("fullName") val fullName: String? = null,
    @SerializedName("cccd") val citizenId: String? = null,
    @SerializedName("email") val email: String? = null,
    @SerializedName("phone") val phone: String? = null,
    @SerializedName("faculty") val faculty: String? = null,
    @SerializedName("academicYear") val academicYear: String? = null,
    
    // Các trường thông tin gia đình và liên hệ khẩn cấp (Mới bổ sung)
    @SerializedName("fatherName") val fatherName: String? = null,
    @SerializedName("fatherPhone") val fatherPhone: String? = null,
    @SerializedName("motherName") val motherName: String? = null,
    @SerializedName("motherPhone") val motherPhone: String? = null,
    @SerializedName("emergencyContact") val emergencyContact: String? = null,
    @SerializedName("permanentAddress") val permanentAddress: String? = null,
    
    @SerializedName("avatarUrl") val avatarUrl: String? = null,
    @SerializedName("status") val status: String? = null,
    
    // Các trường cũ để đảm bảo tương thích
    @SerializedName("gender") val gender: String? = null,
    @SerializedName("birthDate") val birthDate: String? = null,
    @SerializedName("course") val course: String? = null,
    @SerializedName("role") val role: String? = null
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
 * 3. Giao dịch thanh toán
 */
data class Transaction(
    @SerializedName("transaction_id") val transactionId: String?,
    @SerializedName("amount") val amount: Double?,
    @SerializedName("payment_method") val method: String?,
    @SerializedName("status") val status: String?,
    @SerializedName("created_at") val createdAt: String?,
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
 * 4. Yêu cầu cập nhật hồ sơ (Đã bổ sung đầy đủ theo API_DOCUMENTATION.md)
 */
data class UpdateProfileRequest(
    @SerializedName("email") val email: String? = null,
    @SerializedName("phone") val phone: String? = null,
    @SerializedName("fatherName") val fatherName: String? = null,
    @SerializedName("fatherPhone") val fatherPhone: String? = null,
    @SerializedName("motherName") val motherName: String? = null,
    @SerializedName("motherPhone") val motherPhone: String? = null,
    @SerializedName("emergencyContact") val emergencyContact: String? = null,
    @SerializedName("permanentAddress") val permanentAddress: String? = null,
    @SerializedName("avatarUrl") val avatarUrl: String? = null
)
