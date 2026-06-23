package com.ktx.dormitory.core.sync

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import com.ktx.dormitory.data.local.dao.PendingSyncDao
import com.ktx.dormitory.data.local.entity.SyncStatus
import com.ktx.dormitory.domain.model.UpdateProfileRequest
import com.ktx.dormitory.domain.repository.*
import com.ktx.dormitory.core.sync.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val pendingSyncDao: PendingSyncDao,
    private val paymentRepository: PaymentRepository,
    private val userRepository: UserRepository,
    private val accessRepository: AccessRepository
) : CoroutineWorker(appContext, workerParams) {

    private val gson = Gson()

    companion object {
        private val syncMutex = Mutex()
    }

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        syncMutex.withLock {
            val pendingActions = pendingSyncDao.getNotCompletedActions()
                .filter { it.syncStatus == SyncStatus.PENDING }
            
            if (pendingActions.isEmpty()) return@withLock Result.success()

            var hasFailure = false

            for (action in pendingActions) {
                try {
                    pendingSyncDao.updateAction(action.copy(syncStatus = SyncStatus.SYNCING))
                    
                    val success = when (action.actionType) {
                        "VERIFY_PAYMENT" -> {
                            val payload = gson.fromJson(action.payload, VerifyPaymentPayload::class.java)
                            paymentRepository.verifyPayment(
                                payload.billId,
                                payload.amount,
                                payload.method,
                                payload.transactionCode
                            ).isSuccess
                        }
                        "UPDATE_PROFILE" -> {
                            val payload = gson.fromJson(action.payload, UpdateProfileRequest::class.java)
                            userRepository.updateProfile(payload).isSuccess
                        }
                        "REGISTER_FACE" -> {
                            val payload = gson.fromJson(action.payload, RegisterFacePayload::class.java)
                            accessRepository.registerFaceOnServer(payload.studentId, payload.faceImageUrl).isSuccess
                        }
                        else -> true
                    }

                    if (success) {
                        pendingSyncDao.deleteAction(action)
                    } else {
                        val nextRetry = action.retryCount + 1
                        if (nextRetry >= 5) {
                            pendingSyncDao.updateAction(action.copy(syncStatus = SyncStatus.FAILED, retryCount = nextRetry))
                        } else {
                            pendingSyncDao.updateAction(action.copy(syncStatus = SyncStatus.PENDING, retryCount = nextRetry))
                            hasFailure = true
                        }
                    }
                } catch (e: Exception) {
                    hasFailure = true
                    val nextRetry = action.retryCount + 1
                    if (nextRetry >= 5) {
                        pendingSyncDao.updateAction(action.copy(syncStatus = SyncStatus.FAILED, retryCount = nextRetry))
                    } else {
                        pendingSyncDao.updateAction(action.copy(syncStatus = SyncStatus.PENDING, retryCount = nextRetry))
                    }
                }
            }

            if (hasFailure) Result.retry() else Result.success()
        }
    }
}
