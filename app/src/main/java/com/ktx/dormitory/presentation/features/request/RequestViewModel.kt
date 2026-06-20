package com.ktx.dormitory.presentation.features.request

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ktx.dormitory.domain.model.RequestType
import com.ktx.dormitory.domain.repository.RequestRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

@HiltViewModel
class RequestViewModel @Inject constructor(
    private val repository: RequestRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val uiState: StateFlow<RequestUiState> = savedStateHandle.getStateFlow("uiState", RequestUiState())
    val formState: StateFlow<RequestFormState> = savedStateHandle.getStateFlow("formState", RequestFormState())

    private val submitMutex = Mutex()

    private fun updateUiState(reducer: (RequestUiState) -> RequestUiState) {
        savedStateHandle["uiState"] = reducer(uiState.value)
    }

    private fun updateFormState(reducer: (RequestFormState) -> RequestFormState) {
        savedStateHandle["formState"] = reducer(formState.value)
    }

    init {
        if (uiState.value.requests.isEmpty()) {
            loadMyRequests()
        }
    }

    fun loadMyRequests() {
        viewModelScope.launch {
            updateUiState { it.copy(isLoading = true, error = null) }
            repository.getMyRequests()
                .onSuccess { data ->
                    updateUiState { it.copy(isLoading = false, requests = data) }
                }
                .onFailure { e ->
                    updateUiState { it.copy(isLoading = false, error = e.message) }
                }
        }
    }

    fun onContentChange(content: String) {
        updateFormState { it.copy(content = content) }
    }

    fun onTypeChange(type: RequestType) {
        updateFormState { it.copy(type = type) }
    }

    fun submitRequest() {
        val type = formState.value.type
        val content = formState.value.content
        
        viewModelScope.launch {
            if (submitMutex.isLocked) return@launch
            
            submitMutex.withLock {
                updateUiState { it.copy(isSubmitting = true, submitSuccess = false) }
                repository.submitRequest(type, content)
                    .onSuccess {
                        updateUiState { it.copy(isSubmitting = false, submitSuccess = true) }
                        updateFormState { RequestFormState() } // Reset form
                        loadMyRequests()
                    }
                    .onFailure { e ->
                        updateUiState { it.copy(isSubmitting = false, error = e.message) }
                    }
            }
        }
    }

    fun clearStatus() {
        updateUiState { it.copy(submitSuccess = false, error = null) }
    }
}
