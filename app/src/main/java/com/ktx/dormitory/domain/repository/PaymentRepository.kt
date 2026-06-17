package com.ktx.dormitory.domain.repository

import com.ktx.dormitory.domain.model.Invoice

interface PaymentRepository {
    suspend fun getInvoices(): Result<List<Invoice>>
    suspend fun verifyPayment(invoiceId: String): Result<Unit>
}
