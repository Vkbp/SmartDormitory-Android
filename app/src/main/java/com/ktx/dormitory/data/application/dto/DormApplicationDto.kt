package com.ktx.dormitory.data.application.dto

import com.google.gson.annotations.SerializedName

/**
 * DTO cho Đơn đăng ký nội trú.
 * Tương thích với ApplicationResponse.java từ Backend.
 */
data class DormApplicationDto(
    @SerializedName("applicationCode") val applicationCode: String?,
    @SerializedName("status") val status: String?,
    @SerializedName("submittedAt") val submissionDate: String?,
    @SerializedName("revisionDeadline") val revisionDeadline: String?,
    @SerializedName("applicationId") val id: String? = null,
    @SerializedName("fullName") val fullName: String? = null,
    @SerializedName("cccd") val cccd: String? = null,
    @SerializedName("priorityScore") val priorityScore: Int? = null,
    @SerializedName("applicationPdfUrl") val applicationPdfUrl: String? = null
)

data class TimelineStepDto(
    @SerializedName("title") val title: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("timestamp") val timestamp: String?,
    @SerializedName("stepStatus") val stepStatus: String?
)
