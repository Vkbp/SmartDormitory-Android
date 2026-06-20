package com.ktx.dormitory.presentation.features.notification

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ktx.dormitory.domain.repository.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val repository: NotificationRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val uiState: StateFlow<NotificationUiState> = savedStateHandle.getStateFlow("uiState", NotificationUiState())

    private fun updateUiState(reducer: (NotificationUiState) -> NotificationUiState) {
        savedStateHandle["uiState"] = reducer(uiState.value)
    }

    init {
        if (uiState.value.notifications.isEmpty()) {
            loadNotifications()
        }
    }

    fun loadNotifications() {
        viewModelScope.launch {
            updateUiState { it.copy(isLoading = true, error = null) }
            repository.getNotifications()
                .onSuccess { data ->
                    updateUiState { it.copy(isLoading = false, notifications = data) }
                }
                .onFailure { e ->
                    updateUiState { it.copy(isLoading = false, error = e.message) }
                }
        }
    }

    fun markAsRead(id: String) {
        viewModelScope.launch {
            repository.markAsRead(id)
            // Cập nhật UI ngay lập tức từ list hiện tại để tăng cảm giác mượt mà
            updateUiState { state ->
                val updatedList = state.notifications.map {
                    if (it.id == id) it.copy(isRead = true) else it
                }
                state.copy(notifications = updatedList)
            }
        }
    }
}
