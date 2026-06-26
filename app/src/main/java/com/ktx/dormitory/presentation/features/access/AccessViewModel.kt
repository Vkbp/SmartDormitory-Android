package com.ktx.dormitory.presentation.features.access

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ktx.dormitory.data.profile.local.ProfileLocalDataSource
import com.ktx.dormitory.domain.access.repository.AccessRepository
import com.ktx.dormitory.domain.access.usecase.GetAccessHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccessViewModel @Inject constructor(
    private val getAccessHistoryUseCase: GetAccessHistoryUseCase,
    private val repository: AccessRepository,
    private val profileLocalDataSource: ProfileLocalDataSource,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val uiState: StateFlow<AccessUiState> = savedStateHandle.getStateFlow("uiState", AccessUiState())

    private fun updateUiState(reducer: (AccessUiState) -> AccessUiState) {
        savedStateHandle["uiState"] = reducer(uiState.value)
    }

    init {
        // Sync logs flow with UI state
        repository.accessLogs.onEach { logs ->
            updateUiState { it.copy(logs = logs) }
        }.launchIn(viewModelScope)
    }

    fun fetchAccessHistory(studentId: String? = null) {
        viewModelScope.launch {
            updateUiState { it.copy(isLoading = true, error = null) }
            
            val targetId = studentId ?: profileLocalDataSource.getProfile().firstOrNull()?.id
            
            if (targetId == null) {
                updateUiState { it.copy(isLoading = false, error = "Không tìm thấy thông tin sinh viên") }
                return@launch
            }

            getAccessHistoryUseCase(targetId)
                .onFailure { e -> updateUiState { it.copy(error = e.message) } }
            
            updateUiState { it.copy(isLoading = false) }
        }
    }
}
