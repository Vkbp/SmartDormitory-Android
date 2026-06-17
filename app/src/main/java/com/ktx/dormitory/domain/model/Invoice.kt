package com.ktx.dormitory.domain.model

import com.google.gson.annotations.SerializedName

/**
 * Model Hóa đơn (Khớp Spec 2 - Invoices)
 */
data class Invoice(
    @SerializedName("id") val id: Long, // Đổi từ String sang Long theo Spec Backend
    @SerializedName("type") val type: InvoiceType?, // Nullable để tránh crash nếu Backend gửi type lạ
    @SerializedName("amount") val amount: Double,
    @SerializedName("paid_amount") val paidAmount: Double = 0.0,
    @SerializedName("remaining_amount") val remainingAmount: Double = 0.0,
    @SerializedName("status") val status: PaymentStatus?, // Nullable
    @SerializedName("due_date") val dueDate: String, // Định dạng yyyy-MM-dd
    @SerializedName("description") val description: String,
    @SerializedName("room_code") val roomCode: String? = null,
    @SerializedName("bed_code") val bedCode: String? = null
)

enum class InvoiceType {
    @SerializedName("ACCOMMODATION_FEE") ROOM, // Mapping ROOM cũ sang Spec mới
    @SerializedName("ELECTRICITY") ELECTRICITY,
    @SerializedName("WATER") WATER,
    @SerializedName("SERVICE_FEE") SERVICE
}

enum class PaymentStatus {
    @SerializedName("UNPAID") UNPAID,
    @SerializedName("PARTIALLY_PAID") PARTIALLY_PAID,
    @SerializedName("PAID") PAID,
    @SerializedName("OVERDUE") OVERDUE
}
