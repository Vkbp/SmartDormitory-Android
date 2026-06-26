package com.ktx.dormitory.domain.application.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

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
