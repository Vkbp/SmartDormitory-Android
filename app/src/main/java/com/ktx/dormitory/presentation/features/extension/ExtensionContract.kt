package com.ktx.dormitory.presentation.features.extension

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ExtensionUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
) : Parcelable

sealed interface ExtensionUiEvent {
    data class SubmitExtension(val reason: String) : ExtensionUiEvent
    data object ClearStatus : ExtensionUiEvent
}
