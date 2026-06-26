package com.ktx.dormitory.presentation.features.home

import com.ktx.dormitory.domain.model.RoomInfo
import com.ktx.dormitory.domain.model.UserData

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
