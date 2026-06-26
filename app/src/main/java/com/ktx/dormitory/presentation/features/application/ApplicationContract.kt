package com.ktx.dormitory.presentation.features.application

import android.os.Parcelable
import com.ktx.dormitory.domain.application.model.DormApplication
import kotlinx.parcelize.Parcelize

@Parcelize
data class ApplicationUiState(
    val isLoading: Boolean = false,
    val application: DormApplication? = null,
    val error: String? = null
) : Parcelable

sealed interface ApplicationUiEvent {
    data object LoadApplication : ApplicationUiEvent
}
