package com.ktx.dormitory.presentation.features.home

import com.ktx.dormitory.domain.room.model.RoomInfo
import com.ktx.dormitory.domain.auth.model.UserData

interface HomeContract {
    data class State(
        val userData: UserData? = null,
        val roomInfo: RoomInfo? = null,
        val isLoading: Boolean = false,
        val error: String? = null
    )

    sealed interface Event {
        data object RefreshData : Event
    }
}
