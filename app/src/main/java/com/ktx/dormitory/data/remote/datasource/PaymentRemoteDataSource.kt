package com.ktx.dormitory.data.remote.datasource

import com.ktx.dormitory.data.remote.dto.BaseResponse
import com.ktx.dormitory.data.remote.dto.user.InvoiceDto

interface PaymentRemoteDataSource {
    suspend fun getInvoices(): BaseResponse<List<InvoiceDto>>
    suspend fun verifyPayment(invoiceId: String): BaseResponse<Unit>
}
