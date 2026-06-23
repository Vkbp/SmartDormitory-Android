package com.ktx.dormitory.data.mapper

import com.ktx.dormitory.data.local.entity.UserProfileEntity
import com.ktx.dormitory.data.local.entity.InvoiceEntity
import com.ktx.dormitory.data.remote.dto.user.*
import com.ktx.dormitory.domain.model.*

/**
 * Chuyển đổi DTO thành Domain Model cho User
 */
fun StudentResponse.toDomain(): UserProfile {
    return UserProfile(
        id = this.id,
        studentCode = this.studentCode,
        fullName = this.fullName,
        citizenId = this.citizenId,
        email = this.email,
        phone = this.phone,
        faculty = this.faculty,
        academicYear = this.academicYear,
        fatherName = this.fatherName,
        fatherPhone = this.fatherPhone,
        motherName = this.motherName,
        motherPhone = this.motherPhone,
        emergencyContact = this.emergencyContact,
        permanentAddress = this.permanentAddress,
        avatarUrl = this.avatarUrl,
        status = this.status,
        gender = this.gender,
        birthDate = this.birthDate,
        course = this.course,
        role = this.role
    )
}

fun StudentResponse.toEntity(): UserProfileEntity {
    return UserProfileEntity(
        id = this.id ?: "",
        studentCode = this.studentCode ?: "",
        fullName = this.fullName ?: "",
        cccd = this.citizenId ?: "",
        faculty = this.faculty ?: "",
        academicYear = this.academicYear ?: "",
        phone = this.phone ?: "",
        email = this.email ?: "",
        permanentAddress = this.permanentAddress ?: "",
        avatarUrl = this.avatarUrl,
        fatherName = this.fatherName,
        fatherPhone = this.fatherPhone,
        motherName = this.motherName,
        motherPhone = this.motherPhone,
        emergencyContact = this.emergencyContact,
        status = this.status
    )
}

fun UserProfileEntity.toDomain(): UserProfile {
    return UserProfile(
        id = this.id,
        studentCode = this.studentCode,
        fullName = this.fullName,
        citizenId = this.cccd,
        faculty = this.faculty,
        academicYear = this.academicYear,
        phone = this.phone,
        email = this.email,
        permanentAddress = this.permanentAddress,
        avatarUrl = this.avatarUrl,
        fatherName = this.fatherName,
        fatherPhone = this.fatherPhone,
        motherName = this.motherName,
        motherPhone = this.motherPhone,
        emergencyContact = this.emergencyContact,
        status = this.status
    )
}

fun UserProfile.toEntity(): UserProfileEntity {
    return UserProfileEntity(
        id = this.id ?: "",
        studentCode = this.studentCode ?: "",
        fullName = this.fullName ?: "",
        cccd = this.citizenId ?: "",
        faculty = this.faculty ?: "",
        academicYear = this.academicYear ?: "",
        phone = this.phone ?: "",
        email = this.email ?: "",
        permanentAddress = this.permanentAddress ?: "",
        avatarUrl = this.avatarUrl,
        fatherName = this.fatherName,
        fatherPhone = this.fatherPhone,
        motherName = this.motherName,
        motherPhone = this.motherPhone,
        emergencyContact = this.emergencyContact,
        status = this.status
    )
}

fun RoomInfoDto.toDomain(): RoomInfo {
    return RoomInfo(
        building = this.building,
        floor = this.floor,
        roomCode = this.roomCode,
        bedCode = this.bedCode,
        status = this.status
    )
}

fun TransactionDto.toDomain(): Transaction {
    return Transaction(
        transactionId = this.transactionId,
        amount = this.amount,
        method = when (this.type?.uppercase()) {
            "ACCOMMODATION_FEE", "ROOM" -> "Phí phòng"
            "ELECTRIC_FEE", "ELECTRICITY" -> "Tiền điện"
            "WATER_FEE", "WATER" -> "Tiền nước"
            "SERVICE_FEE", "SERVICE" -> "Phí dịch vụ"
            "APPLICATION_FEE" -> "Lệ phí đơn"
            "PENALTY_FEE" -> "Tiền phạt"
            "DEPOSIT_FEE" -> "Tiền cọc"
            else -> this.type ?: "Hóa đơn"
        },
        status = this.status,
        createdAt = this.createdAt, // backend trả về dueDate làm mốc thời gian
        type = this.type,
        message = this.message, // Chứa studentName hoặc mô tả từ backend
        transactionCode = this.transactionCode
    )
}

fun DormApplicationDto.toDomain(): DormApplication {
    return DormApplication(
        applicationCode = this.applicationCode,
        status = this.status,
        submissionDate = this.submissionDate,
        paymentDeadline = this.revisionDeadline, // Backend dùng revisionDeadline làm mốc xử lý tiếp theo
        timeline = emptyList() // Backend hiện trả về list phẳng, không có timeline nested phức tạp
    )
}

fun TimelineStepDto.toDomain(): TimelineStep {
    return TimelineStep(
        title = this.title,
        description = this.description,
        timestamp = this.timestamp,
        stepStatus = when (this.stepStatus?.uppercase()) {
            "COMPLETED" -> AppStepStatus.COMPLETED
            "CURRENT" -> AppStepStatus.CURRENT
            "WAITING" -> AppStepStatus.WAITING
            else -> AppStepStatus.WAITING
        }
    )
}

fun InvoiceDto.toDomain(): Invoice {
    return Invoice(
        id = this.id,
        type = when (this.type?.uppercase()) {
            "ACCOMMODATION_FEE", "ROOM" -> InvoiceType.ROOM
            "ELECTRIC_FEE", "ELECTRICITY" -> InvoiceType.ELECTRICITY
            "WATER_FEE", "WATER" -> InvoiceType.WATER
            "SERVICE_FEE", "SERVICE" -> InvoiceType.SERVICE
            "APPLICATION_FEE" -> InvoiceType.APPLICATION
            "PENALTY_FEE" -> InvoiceType.PENALTY
            "DEPOSIT_FEE" -> InvoiceType.DEPOSIT
            else -> null
        },
        amount = this.amount,
        paidAmount = this.paidAmount,
        remainingAmount = this.remainingAmount,
        status = when (this.status?.uppercase()) {
            "UNPAID" -> PaymentStatus.UNPAID
            "PARTIALLY_PAID" -> PaymentStatus.PARTIALLY_PAID
            "PAID" -> PaymentStatus.PAID
            "OVERDUE" -> PaymentStatus.OVERDUE
            "CANCELLED" -> PaymentStatus.CANCELLED
            else -> null
        },
        dueDate = this.dueDate,
        description = this.description,
        roomCode = this.roomCode,
        bedCode = this.bedCode
    )
}

fun InvoiceDto.toEntity(): InvoiceEntity {
    return InvoiceEntity(
        id = this.id,
        type = this.type,
        amount = this.amount,
        paidAmount = this.paidAmount,
        remainingAmount = this.remainingAmount,
        status = this.status,
        dueDate = this.dueDate,
        description = this.description,
        roomCode = this.roomCode,
        bedCode = this.bedCode
    )
}

fun InvoiceEntity.toDomain(): Invoice {
    return Invoice(
        id = this.id,  // String UUID
        type = when (this.type?.uppercase()) {
            "ACCOMMODATION_FEE", "ROOM" -> InvoiceType.ROOM
            "ELECTRICITY" -> InvoiceType.ELECTRICITY
            "WATER" -> InvoiceType.WATER
            "SERVICE_FEE" -> InvoiceType.SERVICE
            else -> null
        },
        amount = this.amount,
        paidAmount = this.paidAmount,
        remainingAmount = this.remainingAmount,
        status = when (this.status?.uppercase()) {
            "UNPAID" -> PaymentStatus.UNPAID
            "PARTIALLY_PAID" -> PaymentStatus.PARTIALLY_PAID
            "PAID" -> PaymentStatus.PAID
            "OVERDUE" -> PaymentStatus.OVERDUE
            else -> null
        },
        dueDate = this.dueDate,
        description = this.description,
        roomCode = this.roomCode,
        bedCode = this.bedCode
    )
}

/**
 * Mapping từ Domain Model sang DTO cho Update Request
 */
fun com.ktx.dormitory.domain.model.UpdateProfileRequest.toDto(): com.ktx.dormitory.data.remote.dto.user.UpdateProfileRequest {
    return com.ktx.dormitory.data.remote.dto.user.UpdateProfileRequest(
        email = this.email,
        phone = this.phone,
        fatherName = this.fatherName,
        fatherPhone = this.fatherPhone,
        motherName = this.motherName,
        motherPhone = this.motherPhone,
        emergencyContact = this.emergencyContact,
        permanentAddress = this.permanentAddress,
        avatarUrl = this.avatarUrl
    )
}
