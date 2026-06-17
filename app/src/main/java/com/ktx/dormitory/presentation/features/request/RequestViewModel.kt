package com.ktx.dormitory.presentation.features.request

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ktx.dormitory.domain.model.DormRequest
import com.ktx.dormitory.domain.model.RequestType
import com.ktx.dormitory.domain.repository.RequestRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class RequestViewModel @Inject constructor(
    private val repository: RequestRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(RequestFormState())
    val uiState = _uiState.asStateFlow()

    private val _myRequests = MutableStateFlow<List<DormRequest>>(emptyList())
    val myRequests: StateFlow<List<DormRequest>> = _myRequests.asStateFlow()

    init {
        fetchMyRequests()
    }

    fun fetchMyRequests() {
        viewModelScope.launch {
            repository.getMyRequests()
                .onSuccess { list -> _myRequests.value = list }
        }
    }

    fun onContentChange(newContent: String) {
        _uiState.update { it.copy(content = newContent, error = null) }
    }

    fun onTypeChange(newType: RequestType) {
        _uiState.update { it.copy(type = newType) }
    }

    fun submitRequest() {
        val content = _uiState.value.content
        val type = _uiState.value.type

        if (content.isBlank()) {
            _uiState.update { it.copy(error = "Vui lòng nhập nội dung yêu cầu") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            repository.createRequest(type, content)
                .onSuccess {
                    _uiState.update { it.copy(isLoading = false, isSuccess = true, content = "") }
                    fetchMyRequests() // Cập nhật lại danh sách yêu cầu
                }
                .onFailure { exception ->
                    _uiState.update { it.copy(isLoading = false, error = exception.message) }
                }
        }
    }

    fun resetSuccess() { _uiState.update { it.copy(isSuccess = false) } }
}
