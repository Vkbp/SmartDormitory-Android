package com.ktx.dormitory.domain.payment.repository

import com.ktx.dormitory.domain.payment.model.Invoice
import com.ktx.dormitory.domain.payment.model.Transaction

interface PaymentRepository {
    suspend fun getInvoices(): Result<List<Invoice>>
    suspend fun verifyPayment(billId: String, amount: Double, paymentMethod: String, transactionCode: String): Result<Unit>
    suspend fun getPaymentHistory(): Result<List<Transaction>>
}
