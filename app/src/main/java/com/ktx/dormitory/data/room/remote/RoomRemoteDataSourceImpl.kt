package com.ktx.dormitory.data.room.remote

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomRemoteDataSourceImpl @Inject constructor(
    private val api: RoomApiService
) : RoomRemoteDataSource {
    override suspend fun getMyRoom() = api.getMyRoom()
}
