package com.ktx.dormitory.presentation.features.staff

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ktx.dormitory.domain.model.DormRequest
import com.ktx.dormitory.domain.model.RequestStatus
import com.ktx.dormitory.domain.repository.RequestRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StaffApprovalViewModel @Inject constructor(
    private val repository: RequestRepository
) : ViewModel() {

    private val _requests = MutableStateFlow<List<DormRequest>>(emptyList())
    val requests: StateFlow<List<DormRequest>> = _requests.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        fetchRequests()
    }

    fun fetchRequests() {
        viewModelScope.launch {
            _isLoading.value = true
            repository.getAllRequests()
                .onSuccess { _requests.value = it }
                .onFailure { _error.value = it.message }
            _isLoading.value = false
        }
    }

    fun updateStatus(requestId: String, status: RequestStatus) {
        viewModelScope.launch {
            _isLoading.value = true
            repository.updateRequestStatus(requestId, status)
                .onSuccess {
                    fetchRequests() // Refresh list
                }
                .onFailure {
                    _error.value = it.message
                }
            _isLoading.value = false
        }
    }

    fun clearError() {
        _error.value = null
    }
}
