package com.ktx.dormitory.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Model hồ sơ người dùng trong Domain Layer
 */
@Parcelize
data class UserProfile(
    val id: String? = null,
    val studentCode: String? = null,
    val fullName: String? = null,
    val citizenId: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val faculty: String? = null,
    val academicYear: String? = null,
    val fatherName: String? = null,
    val fatherPhone: String? = null,
    val motherName: String? = null,
    val motherPhone: String? = null,
    val emergencyContact: String? = null,
    val permanentAddress: String? = null,
    val avatarUrl: String? = null,
    val status: String? = null,
    val gender: String? = null,
    val birthDate: String? = null,
    val course: String? = null,
    val role: String? = null
) : Parcelable

/**
 * Model thông tin phòng
 */
@Parcelize
data class RoomInfo(
    val building: String?,
    val floor: String?,
    val roomCode: String?,
    val bedCode: String?,
    val status: String?
) : Parcelable

/**
 * Model giao dịch thanh toán
 */
@Parcelize
data class Transaction(
    val transactionId: String?,
    val amount: Double?,
    val method: String?,
    val status: String?,
    val createdAt: String?,
    val type: String? = null,
    val message: String? = null
) : Parcelable

/**
 * Model đơn đăng ký nội trú
 */
@Parcelize
data class DormApplication(
    val applicationCode: String?,
    val status: String?,
    val submissionDate: String?,
    val paymentDeadline: String?,
    val timeline: List<TimelineStep> = emptyList()
) : Parcelable

@Parcelize
data class TimelineStep(
    val title: String?,
    val description: String?,
    val timestamp: String?,
    val stepStatus: AppStepStatus?
) : Parcelable

@Parcelize
enum class AppStepStatus : Parcelable {
    COMPLETED,
    CURRENT,
    WAITING
}

@Parcelize
data class Invoice(
    val id: Long,
    val type: InvoiceType?,
    val amount: Double,
    val paidAmount: Double,
    val remainingAmount: Double,
    val status: PaymentStatus?,
    val dueDate: String?,
    val description: String,
    val roomCode: String? = null,
    val bedCode: String? = null
) : Parcelable

@Parcelize
enum class InvoiceType : Parcelable {
    ROOM, ELECTRICITY, WATER, SERVICE
}

@Parcelize
enum class PaymentStatus : Parcelable {
    UNPAID, PARTIALLY_PAID, PAID, OVERDUE
}

/**
 * Model yêu cầu cập nhật hồ sơ
 */
@Parcelize
data class UpdateProfileRequest(
    val fullName: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val fatherName: String? = null,
    val fatherPhone: String? = null,
    val motherName: String? = null,
    val motherPhone: String? = null,
    val emergencyContact: String? = null,
    val permanentAddress: String? = null,
    val avatarUrl: String? = null
) : Parcelable
