package com.ktx.dormitory.data.remote.dto.user

import com.google.gson.annotations.SerializedName

data class TransactionDto(
    @SerializedName("transaction_id") val transactionId: String?,
    @SerializedName("amount") val amount: Double?,
    @SerializedName("payment_method") val method: String?,
    @SerializedName("status") val status: String?,
    @SerializedName("created_at") val createdAt: String?,
    @SerializedName("type") val type: String? = null,
    @SerializedName("message") val message: String? = null
)
