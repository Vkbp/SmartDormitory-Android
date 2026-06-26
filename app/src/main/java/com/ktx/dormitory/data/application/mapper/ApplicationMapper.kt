package com.ktx.dormitory.data.application.mapper

import com.ktx.dormitory.data.remote.dto.user.DormApplicationDto
import com.ktx.dormitory.data.remote.dto.user.TimelineStepDto
import com.ktx.dormitory.domain.application.model.*

fun DormApplicationDto.toDomain(): DormApplication {
    return DormApplication(
        applicationCode = applicationCode,
        status = status,
        submissionDate = submissionDate,
        paymentDeadline = revisionDeadline,
        timeline = emptyList() // Backend currently returns flat list, not nested
    )
}

fun TimelineStepDto.toDomain(): TimelineStep {
    return TimelineStep(
        title = title,
        description = description,
        timestamp = timestamp,
        stepStatus = when(stepStatus?.uppercase()) {
            "COMPLETED" -> AppStepStatus.COMPLETED
            "CURRENT" -> AppStepStatus.CURRENT
            else -> AppStepStatus.WAITING
        }
    )
}
