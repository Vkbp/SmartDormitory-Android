package com.ktx.dormitory.presentation.features.access

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ktx.dormitory.domain.model.AccessLog
import com.ktx.dormitory.domain.repository.AccessRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccessViewModel @Inject constructor(
    private val repository: AccessRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val isLoading: StateFlow<Boolean> = savedStateHandle.getStateFlow("isLoading", false)
    val error: StateFlow<String?> = savedStateHandle.getStateFlow("error", null)
    val uiMessage: StateFlow<String?> = savedStateHandle.getStateFlow("ui_message", null)

    private fun setLoading(loading: Boolean) {
        savedStateHandle["isLoading"] = loading
    }

    private fun setError(error: String?) {
        savedStateHandle["error"] = error
    }

    private fun setUiMessage(message: String?) {
        savedStateHandle["ui_message"] = message
    }

    // Observe logs từ Room
    val accessHistory: StateFlow<List<AccessLog>> = repository.accessLogs
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        if (accessHistory.value.isEmpty()) {
            fetchAccessHistory()
        }
    }

    fun fetchAccessHistory() {
        viewModelScope.launch {
            setError(null)
            setLoading(true)
            repository.getAccessHistory()
                .onFailure { setError(it.message) }
            setLoading(false)
        }
    }

    fun verifyQrCode(code: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            setLoading(true)
            repository.verifyQrCode(code)
                .onSuccess {
                    setUiMessage("Mở cửa thành công!")
                    onSuccess()
                    fetchAccessHistory() // Load lại lịch sử sau khi ra vào
                }
                .onFailure {
                    setUiMessage(it.message ?: "Mã QR không hợp lệ")
                }
            setLoading(false)
        }
    }

    fun clearMessage() {
        setUiMessage(null)
    }
}
