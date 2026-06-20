package com.ktx.dormitory.data.mapper

import com.ktx.dormitory.data.local.entity.DormRequestEntity
import com.ktx.dormitory.data.remote.dto.common.DormRequestDto
import com.ktx.dormitory.domain.model.*

/**
 * Chuyển đổi DTO thành Domain Model cho Requests
 */
fun DormRequestDto.toDomain(): DormRequest {
    return DormRequest(
        id = this.id,
        studentName = this.studentName,
        studentId = this.studentId,
        type = when (this.type?.uppercase()) {
            "REPAIR" -> RequestType.REPAIR
            "COMPLAINT" -> RequestType.COMPLAINT
            "EXTEND" -> RequestType.EXTEND
            else -> RequestType.OTHER
        },
        content = this.content,
        status = when (this.status.uppercase()) {
            "APPROVED" -> RequestStatus.APPROVED
            "REJECTED" -> RequestStatus.REJECTED
            else -> RequestStatus.PENDING
        },
        createdAt = this.createdAt
    )
}

/**
 * Mapping từ DTO sang Entity (Local)
 */
fun DormRequestDto.toEntity(): DormRequestEntity {
    return DormRequestEntity(
        id = this.id,
        studentName = this.studentName,
        studentId = this.studentId,
        type = this.type,
        content = this.content,
        status = this.status,
        createdAt = this.createdAt
    )
}

/**
 * Mapping từ Entity sang Domain
 */
fun DormRequestEntity.toDomain(): DormRequest {
    return DormRequest(
        id = this.id,
        studentName = this.studentName,
        studentId = this.studentId,
        type = when (this.type?.uppercase()) {
            "REPAIR" -> RequestType.REPAIR
            "COMPLAINT" -> RequestType.COMPLAINT
            "EXTEND" -> RequestType.EXTEND
            else -> RequestType.OTHER
        },
        content = this.content,
        status = when (this.status.uppercase()) {
            "APPROVED" -> RequestStatus.APPROVED
            "REJECTED" -> RequestStatus.REJECTED
            else -> RequestStatus.PENDING
        },
        createdAt = this.createdAt
    )
}

