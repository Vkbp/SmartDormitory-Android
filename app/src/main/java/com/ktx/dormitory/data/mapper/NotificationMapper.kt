package com.ktx.dormitory.data.mapper

import com.ktx.dormitory.data.local.entity.NotificationEntity
import com.ktx.dormitory.data.remote.dto.notification.NotificationResponse
import com.ktx.dormitory.domain.model.Notification

/**
 * Mapper chuyển đổi từ DTO sang Domain Model cho Notification
 */
fun NotificationResponse.toDomain(): Notification {
    return Notification(
        id = this.id ?: "",
        title = this.title ?: this.type ?: "Thông báo",
        message = this.content,
        time = this.createdAt,
        isRead = this.isRead ?: false
    )
}

/**
 * Mapping từ DTO sang Entity (Local)
 */
fun NotificationResponse.toEntity(): NotificationEntity {
    return NotificationEntity(
        id = this.id ?: "",
        title = this.title ?: this.type ?: "Thông báo",
        message = this.content,
        time = this.createdAt,
        isRead = this.isRead ?: false
    )
}

/**
 * Mapping từ Entity sang Domain
 */
fun NotificationEntity.toDomain(): Notification {
    return Notification(
        id = this.id,
        title = this.title,
        message = this.message,
        time = this.time,
        isRead = this.isRead
    )
}
