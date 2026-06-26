package com.ktx.dormitory.data.room.remote

import com.ktx.dormitory.data.common.dto.BaseResponse
import com.ktx.dormitory.data.room.dto.RoomInfoDto
import retrofit2.http.GET

interface RoomApiService {
    @GET("v1/student/room/current")
    suspend fun getMyRoom(): BaseResponse<RoomInfoDto>
}
