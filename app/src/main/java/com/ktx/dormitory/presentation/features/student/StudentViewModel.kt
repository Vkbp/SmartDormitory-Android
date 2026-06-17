package com.ktx.dormitory.presentation.features.student

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ktx.dormitory.domain.model.*
import com.ktx.dormitory.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class StudentUiState(
    val isLoading: Boolean = false,
    val isUploading: Boolean = false,
    val profile: UserProfile? = null,
    val roomInfo: RoomInfo? = null,
    val application: DormApplication? = null,
    val transactions: List<Transaction> = emptyList(),
    val error: String? = null,
    val uploadSuccess: Boolean = false
)

@HiltViewModel
class StudentViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(StudentUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadAllData()
    }

    fun loadAllData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            val profileRes = userRepository.getProfile()
            val roomRes = userRepository.getRoomInfo()
            val appRes = userRepository.getApplicationTimeline()
            val transRes = userRepository.getPaymentHistory()

            _uiState.update { state ->
                state.copy(
                    isLoading = false,
                    profile = profileRes.getOrNull(),
                    roomInfo = roomRes.getOrNull(),
                    application = appRes.getOrNull(),
                    transactions = transRes.getOrDefault(emptyList()),
                    error = if (profileRes.isFailure) "Không thể tải dữ liệu" else null
                )
            }
        }
    }

    fun updateProfile(fullName: String, phone: String, email: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val request = UpdateProfileRequest(fullName, phone, email)
            userRepository.updateProfile(request)
                .onSuccess {
                    // Cập nhật lại state cục bộ sau khi lưu thành công
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            profile = state.profile?.copy(
                                fullName = fullName,
                                phone = phone,
                                email = email
                            )
                        )
                    }
                    onSuccess()
                }
                .onFailure { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.message) }
                }
        }
    }

    fun uploadAvatar(filePath: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isUploading = true, uploadSuccess = false) }
            userRepository.uploadAvatar(filePath)
                .onSuccess { newUrl ->
                    _uiState.update { state ->
                        state.copy(
                            isUploading = false,
                            uploadSuccess = true,
                            profile = state.profile?.copy(avatarUrl = newUrl)
                        )
                    }
                }
                .onFailure { e ->
                    _uiState.update { it.copy(isUploading = false, error = e.message) }
                }
        }
    }

    fun clearUploadStatus() {
        _uiState.update { it.copy(uploadSuccess = false) }
    }
}
