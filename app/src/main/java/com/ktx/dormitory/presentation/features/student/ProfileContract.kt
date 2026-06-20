package com.ktx.dormitory.presentation.features.student

import android.os.Parcelable
import com.ktx.dormitory.domain.model.UserProfile
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProfileUiState(
    val isLoading: Boolean = false,
    val isUploading: Boolean = false,
    val profile: UserProfile? = null,
    val error: String? = null,
    val uploadSuccess: Boolean = false
) : Parcelable

sealed class ProfileUiEvent {
    object LoadProfile : ProfileUiEvent()
    data class UpdateProfile(val name: String, val phone: String, val email: String) : ProfileUiEvent()
    data class UploadAvatar(val path: String) : ProfileUiEvent()
    object ClearUploadStatus : ProfileUiEvent()
}

sealed class ProfileUiEffect {
    data class ShowToast(val message: String) : ProfileUiEffect()
}
