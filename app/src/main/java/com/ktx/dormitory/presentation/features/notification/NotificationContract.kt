package com.ktx.dormitory.presentation.features.notification

import android.os.Parcelable
import com.ktx.dormitory.domain.model.Notification
import kotlinx.parcelize.Parcelize

@Parcelize
data class NotificationUiState(
    val isLoading: Boolean = false,
    val notifications: List<Notification> = emptyList(),
    val error: String? = null
) : Parcelable

sealed class NotificationUiEvent {
    object FetchNotifications : NotificationUiEvent()
    data class MarkAsRead(val id: String) : NotificationUiEvent()
}

sealed class NotificationUiEffect {
    data class ShowError(val message: String) : NotificationUiEffect()
}
