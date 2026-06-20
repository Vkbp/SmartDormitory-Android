package com.ktx.dormitory.data.remote.api

import com.ktx.dormitory.data.remote.dto.BaseResponse
import com.ktx.dormitory.data.remote.dto.user.InvoiceDto
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Interface cho các API liên quan đến Thanh toán
 */
interface PaymentApiService {
    @GET("v1/payments/invoices")
    suspend fun getInvoices(): BaseResponse<List<InvoiceDto>>

    @POST("v1/payments/verify/{invoiceId}")
    suspend fun verifyPayment(@Path("invoiceId") invoiceId: String): BaseResponse<Unit>
}
