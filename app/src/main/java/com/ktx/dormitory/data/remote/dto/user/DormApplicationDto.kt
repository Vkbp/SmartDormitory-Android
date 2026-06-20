package com.ktx.dormitory.data.remote.dto.user

import com.google.gson.annotations.SerializedName

data class DormApplicationDto(
    @SerializedName("application_code") val applicationCode: String?,
    @SerializedName("status") val status: String?,
    @SerializedName("submission_date") val submissionDate: String?,
    @SerializedName("payment_deadline") val paymentDeadline: String?,
    @SerializedName("timeline") val timeline: List<TimelineStepDto> = emptyList()
)

data class TimelineStepDto(
    @SerializedName("title") val title: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("timestamp") val timestamp: String?,
    @SerializedName("step_status") val stepStatus: String?
)
