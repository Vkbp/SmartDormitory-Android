package com.ktx.dormitory.data.room.repository

import com.ktx.dormitory.data.room.mapper.toDomain
import com.ktx.dormitory.data.room.remote.RoomRemoteDataSource
import com.ktx.dormitory.domain.room.model.RoomInfo
import com.ktx.dormitory.domain.room.repository.RoomRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomRepositoryImpl @Inject constructor(
    private val remoteDataSource: RoomRemoteDataSource
) : RoomRepository {

    override suspend fun getRoomInfo(): Result<RoomInfo> {
        return try {
            val response = remoteDataSource.getMyRoom()
            if (response.success && response.data != null) {
                Result.success(response.data.toDomain())
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
