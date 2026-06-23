package com.ktx.dormitory.data.mapper

import com.ktx.dormitory.data.remote.dto.common.AccessLogDto
import com.ktx.dormitory.domain.model.AccessLog

fun AccessLogDto.toDomain() = AccessLog(
    id = id,
    studentId = studentId,
    gateId = gateId,
    buildingId = buildingId,
    eventTimestamp = eventTimestamp,
    decision = decision,
    denialReason = denialReason,
    method = method
)
