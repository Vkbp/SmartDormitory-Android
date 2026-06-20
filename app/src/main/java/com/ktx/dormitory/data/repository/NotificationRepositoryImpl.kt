package com.ktx.dormitory.data.repository

import com.ktx.dormitory.core.network.NetworkMonitor
import com.ktx.dormitory.core.sync.SyncScheduler
import com.ktx.dormitory.data.local.dao.NotificationDao
import com.ktx.dormitory.data.local.dao.PendingSyncDao
import com.ktx.dormitory.data.local.entity.PendingSyncEntity
import com.ktx.dormitory.data.remote.api.NotificationApiService
import com.ktx.dormitory.data.mapper.*
import com.ktx.dormitory.domain.model.Notification
import com.ktx.dormitory.domain.repository.NotificationRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationRepositoryImpl @Inject constructor(
    private val api: NotificationApiService,
    private val localDao: NotificationDao,
    private val pendingSyncDao: PendingSyncDao,
    private val syncScheduler: SyncScheduler,
    private val networkMonitor: NetworkMonitor
) : NotificationRepository {

    override suspend fun getNotifications(): Result<List<Notification>> {
        return try {
            val response = api.getNotifications()
            if (response.success && response.data != null) {
                val notifications = response.data.map { it.toDomain() }
                // Lưu vào cache local
                localDao.insertNotifications(response.data.map { it.toEntity() })
                Result.success(notifications)
            } else {
                // Trả về cache nếu API lỗi
                val cached = localDao.getAllNotifications().first()
                Result.success(cached.map { it.toDomain() })
            }
        } catch (e: Exception) {
            // Trả về cache nếu mất mạng
            val cached = localDao.getAllNotifications().first()
            if (cached.isNotEmpty()) {
                Result.success(cached.map { it.toDomain() })
            } else {
                Result.failure(e)
            }
        }
    }

    override suspend fun markAsRead(notificationId: String): Result<Unit> {
        return try {
            val isOnline = networkMonitor.isOnline.first()
            if (!isOnline) {
                localDao.markAsRead(notificationId)
                return queueAction("MARK_NOTIFICATION_READ", notificationId)
            }
            val response = api.markAsRead(notificationId)
            if (response.success) {
                localDao.markAsRead(notificationId)
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            localDao.markAsRead(notificationId)
            if (isNetworkException(e)) {
                queueAction("MARK_NOTIFICATION_READ", notificationId)
            } else {
                Result.failure(e)
            }
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
