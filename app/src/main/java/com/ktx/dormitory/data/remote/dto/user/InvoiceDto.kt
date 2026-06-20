package com.ktx.dormitory.data.remote.dto.user

import com.google.gson.annotations.SerializedName

data class InvoiceDto(
    @SerializedName("id") val id: Long,
    @SerializedName("type") val type: String?,
    @SerializedName("amount") val amount: Double,
    @SerializedName("paid_amount") val paidAmount: Double = 0.0,
    @SerializedName("remaining_amount") val remainingAmount: Double = 0.0,
    @SerializedName("status") val status: String?,
    @SerializedName("due_date") val dueDate: String,
    @SerializedName("description") val description: String,
    @SerializedName("room_code") val roomCode: String? = null,
    @SerializedName("bed_code") val bedCode: String? = null
)
