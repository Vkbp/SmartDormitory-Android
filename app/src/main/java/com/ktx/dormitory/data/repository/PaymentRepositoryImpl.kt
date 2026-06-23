package com.ktx.dormitory.data.repository

import com.ktx.dormitory.core.network.NetworkMonitor
import com.ktx.dormitory.core.sync.SyncScheduler
import com.ktx.dormitory.data.local.dao.InvoiceDao
import com.ktx.dormitory.data.local.dao.PendingSyncDao
import com.ktx.dormitory.data.local.entity.PendingSyncEntity
import com.ktx.dormitory.data.remote.datasource.PaymentRemoteDataSource
import com.ktx.dormitory.data.mapper.toDomain
import com.ktx.dormitory.data.mapper.toEntity
import com.ktx.dormitory.domain.model.Invoice
import com.ktx.dormitory.domain.repository.PaymentRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PaymentRepositoryImpl @Inject constructor(
    private val remoteDataSource: PaymentRemoteDataSource,
    private val localDao: InvoiceDao,
    private val pendingSyncDao: PendingSyncDao,
    private val syncScheduler: SyncScheduler,
    private val networkMonitor: NetworkMonitor
) : PaymentRepository {

    override suspend fun getInvoices(): Result<List<Invoice>> {
        return try {
            val response = remoteDataSource.getInvoices()
            if (response.success && response.data != null) {
                val invoices = response.data.map { it.toDomain() }
                localDao.insertInvoices(response.data.map { it.toEntity() })
                Result.success(invoices)
            } else {
                val cached = localDao.getAllInvoices().first()
                Result.success(cached.map { it.toDomain() })
            }
        } catch (e: Exception) {
            val cached = localDao.getAllInvoices().first()
            if (cached.isNotEmpty()) {
                Result.success(cached.map { it.toDomain() })
            } else {
                Result.failure(e)
            }
        }
    }

    override suspend fun verifyPayment(
        billId: String,
        amount: Double,
        paymentMethod: String,
        transactionCode: String
    ): Result<Unit> {
        return try {
            val response = remoteDataSource.verifyPayment(billId, amount, paymentMethod, transactionCode)
            if (response.success) Result.success(Unit) else Result.failure(Exception(response.message))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun queueAction(actionType: String, payload: String): Result<Unit> {
        pendingSyncDao.insertAction(
            PendingSyncEntity(
                actionType = actionType,
                payload = payload
            )
        )
        syncScheduler.scheduleSync()
        return Result.success(Unit)
    }

    private fun isNetworkException(e: Exception): Boolean {
        return e is java.io.IOException
    }
}
