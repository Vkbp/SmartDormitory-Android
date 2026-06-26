package com.ktx.dormitory.data.payment.repository

import com.ktx.dormitory.data.local.dao.InvoiceDao
import com.ktx.dormitory.data.payment.mapper.*
import com.ktx.dormitory.data.payment.remote.PaymentRemoteDataSource
import com.ktx.dormitory.domain.payment.model.*
import com.ktx.dormitory.domain.payment.repository.PaymentRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PaymentRepositoryImpl @Inject constructor(
    private val remoteDataSource: PaymentRemoteDataSource,
    private val invoiceDao: InvoiceDao
) : PaymentRepository {

    override suspend fun getInvoices(): Result<List<Invoice>> {
        return try {
            val response = remoteDataSource.getInvoices()
            if (response.success && response.data != null) {
                val invoices = response.data.map { it.toDomain() }
                invoiceDao.insertInvoices(response.data.map { it.toEntity() })
                Result.success(invoices)
            } else {
                val cached = invoiceDao.getAllInvoices().first()
                Result.success(cached.map { it.toDomain() })
            }
        } catch (e: Exception) {
            val cached = invoiceDao.getAllInvoices().first()
            if (cached.isNotEmpty()) {
                Result.success(cached.map { it.toDomain() })
            } else {
                Result.failure(e)
            }
        }
    }

    override suspend fun verifyPayment(billId: String, amount: Double, paymentMethod: String, transactionCode: String): Result<Unit> {
        return try {
            val response = remoteDataSource.verifyPayment(billId, amount, paymentMethod, transactionCode)
            if (response.success) Result.success(Unit) else Result.failure(Exception(response.message))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getPaymentHistory(): Result<List<Transaction>> {
        return try {
            val response = remoteDataSource.getPaymentHistory()
            if (response.success && response.data != null) {
                Result.success(response.data.map { it.toDomain() })
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
