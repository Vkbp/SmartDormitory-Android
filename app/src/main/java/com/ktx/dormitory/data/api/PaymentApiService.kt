package com.ktx.dormitory.data.api

import com.ktx.dormitory.domain.model.BaseResponse
import com.ktx.dormitory.domain.model.Invoice
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Đã chuẩn hóa Endpoint theo v1/
 */
interface PaymentApiService {
    @GET("v1/payments/invoices")
    suspend fun getInvoices(): BaseResponse<List<Invoice>>

    @POST("v1/payments/verify/{invoiceId}")
    suspend fun verifyPayment(@Path("invoiceId") invoiceId: String): BaseResponse<Unit>
}
