package com.ktx.dormitory.data.remote.datasource

import com.ktx.dormitory.data.remote.api.PaymentApiService
import com.ktx.dormitory.data.remote.dto.BaseResponse
import com.ktx.dormitory.data.remote.dto.user.InvoiceDto
import javax.inject.Inject

class PaymentRemoteDataSourceImpl @Inject constructor(
    private val api: PaymentApiService
) : PaymentRemoteDataSource {
    override suspend fun getInvoices(): BaseResponse<List<InvoiceDto>> = api.getInvoices()
    override suspend fun verifyPayment(invoiceId: String): BaseResponse<Unit> = api.verifyPayment(invoiceId)
}
