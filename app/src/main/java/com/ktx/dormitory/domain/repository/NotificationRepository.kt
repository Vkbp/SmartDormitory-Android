package com.ktx.dormitory.domain.repository

import com.ktx.dormitory.domain.model.Notification

interface NotificationRepository {
    suspend fun getNotifications(): Result<List<Notification>>
    suspend fun markAsRead(notificationId: String): Result<Unit>
}