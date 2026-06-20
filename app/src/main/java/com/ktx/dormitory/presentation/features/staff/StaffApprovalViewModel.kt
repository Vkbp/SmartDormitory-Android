package com.ktx.dormitory.presentation.features.staff

import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ktx.dormitory.domain.model.DormRequest
import com.ktx.dormitory.domain.model.RequestStatus
import com.ktx.dormitory.domain.repository.RequestRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

@Parcelize
data class StaffApprovalUiState(
    val requests: List<DormRequest> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
) : Parcelable

@HiltViewModel
class StaffApprovalViewModel @Inject constructor(
    private val repository: RequestRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val uiState: StateFlow<StaffApprovalUiState> = savedStateHandle.getStateFlow("uiState", StaffApprovalUiState())

    private fun updateUiState(reducer: (StaffApprovalUiState) -> StaffApprovalUiState) {
        savedStateHandle["uiState"] = reducer(uiState.value)
    }

    init {
        if (uiState.value.requests.isEmpty()) {
            fetchRequests()
        }
    }

    fun fetchRequests() {
        viewModelScope.launch {
            updateUiState { it.copy(isLoading = true, error = null) }
            repository.getPendingRequests()
                .onSuccess { data -> 
                    updateUiState { it.copy(isLoading = false, requests = data) } 
                }
                .onFailure { e -> 
                    updateUiState { it.copy(isLoading = false, error = e.message) } 
                }
        }
    }

    fun updateStatus(requestId: String, status: RequestStatus) {
        viewModelScope.launch {
            updateUiState { it.copy(isLoading = true) }
            repository.updateRequestStatus(requestId, status)
                .onSuccess {
                    fetchRequests() // Refresh list
                }
                .onFailure { e ->
                    updateUiState { it.copy(isLoading = false, error = e.message) }
                }
        }
    }

    fun clearError() {
        updateUiState { it.copy(error = null) }
    }
}
