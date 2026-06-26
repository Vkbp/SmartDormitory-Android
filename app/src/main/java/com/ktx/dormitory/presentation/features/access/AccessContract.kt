package com.ktx.dormitory.presentation.features.access

import android.os.Parcelable
import com.ktx.dormitory.domain.access.model.AccessLog
import kotlinx.parcelize.Parcelize

@Parcelize
data class AccessUiState(
    val isLoading: Boolean = false,
    val logs: List<AccessLog> = emptyList(),
    val error: String? = null,
    val uiMessage: String? = null
) : Parcelable

sealed interface AccessUiEvent {
    data object FetchHistory : AccessUiEvent
    data class RegisterFace(val imageUrl: String) : AccessUiEvent
}
