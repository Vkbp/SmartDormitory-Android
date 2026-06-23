package com.ktx.dormitory.domain.repository

import com.ktx.dormitory.domain.model.Invoice

interface PaymentRepository {
    suspend fun getInvoices(): Result<List<Invoice>>
    suspend fun verifyPayment(billId: String, amount: Double, paymentMethod: String, transactionCode: String): Result<Unit>
}
