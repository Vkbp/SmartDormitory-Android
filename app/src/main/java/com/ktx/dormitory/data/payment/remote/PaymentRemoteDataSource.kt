package com.ktx.dormitory.data.payment.remote

import com.ktx.dormitory.data.common.dto.BaseResponse
import com.ktx.dormitory.data.payment.dto.InvoiceDto
import com.ktx.dormitory.data.payment.dto.TransactionDto

interface PaymentRemoteDataSource {
    suspend fun getInvoices(): BaseResponse<List<InvoiceDto>>
    suspend fun verifyPayment(billId: String, amount: Double, paymentMethod: String, transactionCode: String): BaseResponse<Unit>
    suspend fun getPaymentHistory(): BaseResponse<List<TransactionDto>>
}
