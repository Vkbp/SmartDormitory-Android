package com.ktx.dormitory.data.mapper

import com.ktx.dormitory.data.remote.dto.common.AccessLogDto
import com.ktx.dormitory.domain.model.AccessLog

fun AccessLogDto.toDomain() = AccessLog(
    id = id,
    userId = userId,
    location = location,
    timestamp = timestamp,
    isSuccess = isSuccess,
    method = method
)
