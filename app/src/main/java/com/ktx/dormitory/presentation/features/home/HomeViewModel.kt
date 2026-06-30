package com.ktx.dormitory.presentation.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ktx.dormitory.domain.auth.usecase.GetAuthStateUseCase
import com.ktx.dormitory.domain.room.usecase.GetRoomInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAuthStateUseCase: GetAuthStateUseCase,
    private val getRoomInfoUseCase: GetRoomInfoUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeContract.State())
    val uiState: StateFlow<HomeContract.State> = _uiState.asStateFlow()

    init {
        loadHomeData()
    }

    fun loadHomeData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            val authResult = getAuthStateUseCase()
            authResult.onSuccess { userData ->
                _uiState.update { it.copy(userData = userData) }
                
                val roomResult = getRoomInfoUseCase()
                roomResult.onSuccess { roomInfo ->
                    _uiState.update { it.copy(roomInfo = roomInfo, isLoading = false) }
                }.onFailure { e ->
                    _uiState.update { it.copy(error = e.message, isLoading = false) }
                }
            }.onFailure { e ->
                _uiState.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun onEvent(event: HomeContract.Event) {
        when (event) {
            HomeContract.Event.RefreshData -> loadHomeData()
        }
    }
}
