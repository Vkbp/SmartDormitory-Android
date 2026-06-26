package com.ktx.dormitory.data.payment.remote

import com.ktx.dormitory.data.common.dto.BaseResponse
import com.ktx.dormitory.data.payment.dto.InvoiceDto
import com.ktx.dormitory.data.payment.dto.TransactionDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface PaymentApiService {
    @GET("v1/bills")
    suspend fun getInvoices(): BaseResponse<List<InvoiceDto>>

    @POST("payments/online")
    suspend fun verifyPayment(@Body request: HashMap<String, Any>): BaseResponse<Unit>

    @GET("v1/bills")
    suspend fun getPaymentHistory(): BaseResponse<List<TransactionDto>>
}
