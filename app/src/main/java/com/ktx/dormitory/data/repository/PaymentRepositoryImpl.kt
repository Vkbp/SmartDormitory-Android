package com.ktx.dormitory.data.repository

import com.ktx.dormitory.core.network.toUserFriendlyMessage
import com.ktx.dormitory.data.api.PaymentApiService
import com.ktx.dormitory.domain.model.Invoice
import com.ktx.dormitory.domain.repository.PaymentRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PaymentRepositoryImpl @Inject constructor(
    private val api: PaymentApiService
) : PaymentRepository {
    override suspend fun getInvoices(): Result<List<Invoice>> {
        return try {
            val response = api.getInvoices()
            if (response.success && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(Exception(e.toUserFriendlyMessage()))
        }
    }

    override suspend fun verifyPayment(invoiceId: String): Result<Unit> {
        return try {
            val response = api.verifyPayment(invoiceId)
            if (response.success) {
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(Exception(e.toUserFriendlyMessage()))
        }
    }
}
