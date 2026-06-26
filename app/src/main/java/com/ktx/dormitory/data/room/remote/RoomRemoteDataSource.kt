package com.ktx.dormitory.data.room.remote

import com.ktx.dormitory.data.common.dto.BaseResponse
import com.ktx.dormitory.data.room.dto.RoomInfoDto

interface RoomRemoteDataSource {
    suspend fun getMyRoom(): BaseResponse<RoomInfoDto>
}
