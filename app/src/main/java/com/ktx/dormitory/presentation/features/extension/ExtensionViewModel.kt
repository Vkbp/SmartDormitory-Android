package com.ktx.dormitory.presentation.features.extension

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ktx.dormitory.data.profile.local.ProfileLocalDataSource
import com.ktx.dormitory.domain.extension.usecase.RequestExtensionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExtensionViewModel @Inject constructor(
    private val requestExtensionUseCase: RequestExtensionUseCase,
    private val profileLocalDataSource: ProfileLocalDataSource,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val uiState: StateFlow<ExtensionUiState> = savedStateHandle.getStateFlow("uiState", ExtensionUiState())

    private fun updateUiState(reducer: (ExtensionUiState) -> ExtensionUiState) {
        savedStateHandle["uiState"] = reducer(uiState.value)
    }

    fun onEvent(event: ExtensionUiEvent) {
        when (event) {
            is ExtensionUiEvent.SubmitExtension -> submitExtension(event.reason)
            ExtensionUiEvent.ClearStatus -> updateUiState { it.copy(isSuccess = false, error = null) }
        }
    }

    private fun submitExtension(reason: String) {
        viewModelScope.launch {
            updateUiState { it.copy(isLoading = true, error = null) }
            val studentId = profileLocalDataSource.getProfile().firstOrNull()?.id
            
            if (studentId == null) {
                updateUiState { it.copy(isLoading = false, error = "Không tìm thấy thông tin sinh viên") }
                return@launch
            }

            requestExtensionUseCase(studentId, reason)
                .onSuccess { updateUiState { it.copy(isLoading = false, isSuccess = true) } }
                .onFailure { e -> updateUiState { it.copy(isLoading = false, error = e.message) } }
        }
    }
}
