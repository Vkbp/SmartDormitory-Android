package com.ktx.dormitory.domain.usecase.notification

import com.ktx.dormitory.domain.model.Notification
import com.ktx.dormitory.domain.repository.NotificationRepository
import javax.inject.Inject

class GetNotificationsUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(): Result<List<Notification>> {
        return repository.getNotifications()
    }
}
