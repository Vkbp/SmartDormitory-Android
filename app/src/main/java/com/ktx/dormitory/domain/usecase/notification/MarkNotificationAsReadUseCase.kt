package com.ktx.dormitory.domain.usecase.notification

import com.ktx.dormitory.domain.repository.NotificationRepository
import javax.inject.Inject

class MarkNotificationAsReadUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(notificationId: String): Result<Unit> {
        return repository.markAsRead(notificationId)
    }
}
