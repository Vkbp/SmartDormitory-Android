package com.ktx.dormitory.presentation.features.room

import android.os.Parcelable
import com.ktx.dormitory.domain.room.model.RoomInfo
import kotlinx.parcelize.Parcelize

@Parcelize
data class RoomUiState(
    val isLoading: Boolean = false,
    val roomInfo: RoomInfo? = null,
    val error: String? = null
) : Parcelable

sealed interface RoomUiEvent {
    data object LoadRoomInfo : RoomUiEvent
}
