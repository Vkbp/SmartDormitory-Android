package com.ktx.dormitory.presentation.features.profile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ktx.dormitory.domain.profile.usecase.GetProfileUseCase
import com.ktx.dormitory.domain.profile.usecase.UpdateProfileUseCase
import com.ktx.dormitory.domain.profile.usecase.UploadAvatarUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getProfileUseCase: GetProfileUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase,
    private val uploadAvatarUseCase: UploadAvatarUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val uiState: StateFlow<ProfileUiState> = savedStateHandle.getStateFlow("uiState", ProfileUiState())

    private fun updateUiState(reducer: (ProfileUiState) -> ProfileUiState) {
        savedStateHandle["uiState"] = reducer(uiState.value)
    }

    init {
        if (uiState.value.profile == null) {
            loadProfile()
        }
    }

    fun loadProfile() {
        viewModelScope.launch {
            updateUiState { it.copy(isLoading = true, error = null) }
            getProfileUseCase()
                .onSuccess { profile ->
                    updateUiState { it.copy(isLoading = false, profile = profile) }
                }
                .onFailure { e ->
                    updateUiState { it.copy(isLoading = false, error = e.message) }
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
