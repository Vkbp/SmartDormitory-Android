package com.ktx.dormitory.presentation.features.student

import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ktx.dormitory.domain.model.DormApplication
import com.ktx.dormitory.domain.usecase.user.GetApplicationTimelineUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

@Parcelize
data class ApplicationUiState(
    val isLoading: Boolean = false,
    val application: DormApplication? = null,
    val error: String? = null
) : Parcelable

@HiltViewModel
class ApplicationViewModel @Inject constructor(
    private val getApplicationTimelineUseCase: GetApplicationTimelineUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val uiState: StateFlow<ApplicationUiState> = savedStateHandle.getStateFlow("uiState", ApplicationUiState())

    private fun updateUiState(reducer: (ApplicationUiState) -> ApplicationUiState) {
        savedStateHandle["uiState"] = reducer(uiState.value)
    }

    init {
        if (uiState.value.application == null) {
            loadApplication()
        }
    }

    fun loadApplication() {
        viewModelScope.launch {
            updateUiState { it.copy(isLoading = true, error = null) }
            getApplicationTimelineUseCase()
                .onSuccess { app ->
                    updateUiState { it.copy(isLoading = false, application = app) }
                }
                .onFailure { e ->
                    updateUiState { it.copy(isLoading = false, error = e.message) }
                }
        }
    }
}
