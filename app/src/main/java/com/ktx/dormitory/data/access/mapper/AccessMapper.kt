package com.ktx.dormitory.data.access.mapper

import com.ktx.dormitory.data.local.entity.AccessLogEntity
import com.ktx.dormitory.data.remote.dto.common.AccessLogDto
import com.ktx.dormitory.domain.access.model.AccessLog

fun AccessLogDto.toDomain() = AccessLog(
    id = id ?: "",
    studentId = studentId,
    gateId = gateId,
    buildingId = buildingId,
    eventTimestamp = eventTimestamp,
    decision = decision,
    denialReason = denialReason,
    method = method
)

fun AccessLogEntity.toDomain() = AccessLog(
    id = id,
    studentId = studentId,
    gateId = gateId,
    buildingId = buildingId,
    eventTimestamp = eventTimestamp,
    decision = decision,
    denialReason = denialReason,
    method = method
)

fun AccessLogDto.toEntity() = AccessLogEntity(
    id = id ?: "",
    studentId = studentId,
    gateId = gateId,
    buildingId = buildingId,
    eventTimestamp = eventTimestamp,
    decision = decision,
    denialReason = denialReason,
    method = method
)
