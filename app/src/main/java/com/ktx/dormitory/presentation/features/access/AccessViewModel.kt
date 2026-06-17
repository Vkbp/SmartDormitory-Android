package com.ktx.dormitory.presentation.features.access

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ktx.dormitory.domain.model.AccessLog
import com.ktx.dormitory.domain.repository.AccessRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccessViewModel @Inject constructor(
    private val repository: AccessRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    var isLoading by mutableStateOf(false)
        private set

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // Key để lưu state qua quá trình recreation
    private val KEY_UI_MESSAGE = "ui_message"

    // Sử dụng StateFlow từ SavedStateHandle để đảm bảo message không mất khi xoay màn hình/process death
    val uiMessage: StateFlow<String?> = savedStateHandle.getStateFlow(KEY_UI_MESSAGE, null)

    // Observe logs từ Room
    val accessHistory: StateFlow<List<AccessLog>> = repository.accessLogs
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        fetchAccessHistory()
    }

    fun fetchAccessHistory() {
        viewModelScope.launch {
            _error.value = null
            isLoading = true
            repository.getAccessHistory()
                .onFailure { _error.value = it.message }
            isLoading = false
        }
    }

    fun verifyQrCode(code: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading = true
            repository.verifyQr(code)
                .onSuccess {
                    savedStateHandle[KEY_UI_MESSAGE] = "Mở cửa thành công!"
                    onSuccess()
                    fetchAccessHistory() // Load lại lịch sử sau khi ra vào
                }
                .onFailure {
                    savedStateHandle[KEY_UI_MESSAGE] = it.message ?: "Mã QR không hợp lệ"
                }
            isLoading = false
        }
    }

    fun clearMessage() {
        savedStateHandle[KEY_UI_MESSAGE] = null
    }
}
