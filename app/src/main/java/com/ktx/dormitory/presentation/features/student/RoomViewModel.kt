package com.ktx.dormitory.presentation.features.student

import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ktx.dormitory.domain.model.RoomInfo
import com.ktx.dormitory.domain.usecase.user.GetRoomInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

@Parcelize
data class RoomUiState(
    val isLoading: Boolean = false,
    val roomInfo: RoomInfo? = null,
    val error: String? = null
) : Parcelable

@HiltViewModel
class RoomViewModel @Inject constructor(
    private val getRoomInfoUseCase: GetRoomInfoUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val uiState: StateFlow<RoomUiState> = savedStateHandle.getStateFlow("uiState", RoomUiState())

    private fun updateUiState(reducer: (RoomUiState) -> RoomUiState) {
        savedStateHandle["uiState"] = reducer(uiState.value)
    }

    init {
        if (uiState.value.roomInfo == null) {
            loadRoomInfo()
        }
    }

    fun loadRoomInfo() {
        viewModelScope.launch {
            updateUiState { it.copy(isLoading = true, error = null) }
            getRoomInfoUseCase()
                .onSuccess { info ->
                    updateUiState { it.copy(isLoading = false, roomInfo = info) }
                }
                .onFailure { e ->
                    updateUiState { it.copy(isLoading = false, error = e.message) }
                }
        }
    }
}
