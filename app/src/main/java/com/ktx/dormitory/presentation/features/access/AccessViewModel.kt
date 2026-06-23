package com.ktx.dormitory.presentation.features.access

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ktx.dormitory.data.local.datasource.UserLocalDataSource
import com.ktx.dormitory.domain.model.AccessLog
import com.ktx.dormitory.domain.repository.AccessRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccessViewModel @Inject constructor(
    private val repository: AccessRepository,
    private val userLocalDataSource: UserLocalDataSource,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val isLoading: StateFlow<Boolean> = savedStateHandle.getStateFlow("isLoading", false)
    val error: StateFlow<String?> = savedStateHandle.getStateFlow("error", null)
    val uiMessage: StateFlow<String?> = savedStateHandle.getStateFlow("ui_message", null)

    private fun setLoading(loading: Boolean) { savedStateHandle["isLoading"] = loading }
    private fun setError(error: String?) { savedStateHandle["error"] = error }
    private fun setUiMessage(message: String?) { savedStateHandle["ui_message"] = message }

    val accessHistory: StateFlow<List<AccessLog>> = repository.accessLogs
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    /**
     * Tải lịch sử ra vào. Tự động lấy studentId từ profile.
     */
    fun fetchAccessHistory(studentId: String? = null) {
        viewModelScope.launch {
            setError(null)
            setLoading(true)
            
            val targetId = studentId ?: userLocalDataSource.getProfile().firstOrNull()?.id
            
            if (targetId == null) {
                setError("Không tìm thấy thông tin sinh viên")
                setLoading(false)
                return@launch
            }

            repository.getAccessHistory(targetId)
                .onFailure { setError(it.message) }
            setLoading(false)
        }
    }

    /**
     * Đăng ký khuôn mặt: gửi URL ảnh đã upload lên server.
     * @param faceImageUrl URL ảnh sau khi upload thành công.
     */
    fun registerFace(faceImageUrl: String) {
        viewModelScope.launch {
            setLoading(true)
            val profile = userLocalDataSource.getProfile().firstOrNull()
            val studentId = profile?.id

            if (studentId == null) {
                setUiMessage("Lỗi: Không tìm thấy thông tin sinh viên")
                setLoading(false)
                return@launch
            }

            repository.registerFaceOnServer(studentId, faceImageUrl)
                .onSuccess { setUiMessage("Đăng ký khuôn mặt thành công!") }
                .onFailure { setUiMessage(it.message ?: "Đăng ký thất bại") }
            setLoading(false)
        }
    }

    fun clearMessage() { setUiMessage(null) }
}
