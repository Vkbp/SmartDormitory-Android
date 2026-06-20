package com.ktx.dormitory.presentation.features.student

import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ktx.dormitory.domain.model.*
import com.ktx.dormitory.domain.usecase.user.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

@Parcelize
data class StudentUiState(
    val isLoading: Boolean = false,
    val isUploading: Boolean = false,
    val profile: UserProfile? = null,
    val roomInfo: RoomInfo? = null,
    val application: DormApplication? = null,
    val transactions: List<Transaction> = emptyList(),
    val error: String? = null,
    val uploadSuccess: Boolean = false
) : Parcelable

@HiltViewModel
class StudentViewModel @Inject constructor(
    private val getProfileUseCase: GetProfileUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase,
    private val getRoomInfoUseCase: GetRoomInfoUseCase,
    private val getApplicationTimelineUseCase: GetApplicationTimelineUseCase,
    private val getPaymentHistoryUseCase: GetPaymentHistoryUseCase,
    private val uploadAvatarUseCase: UploadAvatarUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val uiState: StateFlow<StudentUiState> = savedStateHandle.getStateFlow("uiState", StudentUiState())

    private fun updateUiState(reducer: (StudentUiState) -> StudentUiState) {
        savedStateHandle["uiState"] = reducer(uiState.value)
    }

    init {
        if (uiState.value.profile == null) {
            loadAllData()
        }
    }

    fun loadAllData() {
        viewModelScope.launch {
            updateUiState { it.copy(isLoading = true, error = null) }
            
            val profileRes = getProfileUseCase()
            val roomRes = getRoomInfoUseCase()
            val appRes = getApplicationTimelineUseCase()
            val transRes = getPaymentHistoryUseCase()

            updateUiState { state ->
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
            updateUiState { it.copy(isLoading = true) }
            updateProfileUseCase(fullName, phone, email)
                .onSuccess {
                    updateUiState { state ->
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
                    updateUiState { it.copy(isLoading = false, error = e.message) }
                }
        }
    }

    fun uploadAvatar(filePath: String) {
        viewModelScope.launch {
            updateUiState { it.copy(isUploading = true, uploadSuccess = false) }
            uploadAvatarUseCase(filePath)
                .onSuccess { newUrl ->
                    updateUiState { state ->
                        state.copy(
                            isUploading = false,
                            uploadSuccess = true,
                            profile = state.profile?.copy(avatarUrl = newUrl)
                        )
                    }
                }
                .onFailure { e ->
                    updateUiState { it.copy(isUploading = false, error = e.message) }
                }
        }
    }

    fun clearUploadStatus() {
        updateUiState { it.copy(uploadSuccess = false) }
    }
}
