package com.ktx.dormitory.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "invoices")
data class InvoiceEntity(
    @PrimaryKey val id: String,  // UUID from backend
    val type: String?,
    val amount: Double?,
    val paidAmount: Double?,
    val remainingAmount: Double?,
    val status: String?,
    val dueDate: String?,
    val description: String?,
    val roomCode: String?,
    val bedCode: String?
)
