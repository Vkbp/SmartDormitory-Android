package com.ktx.dormitory.data.remote.api

import com.ktx.dormitory.data.remote.dto.BaseResponse
import com.ktx.dormitory.data.remote.dto.user.InvoiceDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * Interface cho các API liên quan đến Thanh toán.
 * Đồng bộ với Backend:
 *   GET  v1/bills           - Lấy danh sách hóa đơn
 *   POST v1/payments/online - Tạo giao dịch thanh toán online
 */
interface PaymentApiService {

    @GET("v1/bills")
    suspend fun getInvoices(): BaseResponse<List<InvoiceDto>>

    /**
     * Ghi nhận thanh toán online (VietQR).
     * Body: { billId, amount, paymentMethod, transactionCode }
     */
    @POST("payments/online")
    suspend fun verifyPayment(@Body request: HashMap<String, Any>): BaseResponse<Unit>
}
