package com.ktx.dormitory.data.profile.local

import com.ktx.dormitory.data.local.entity.UserProfileEntity
import kotlinx.coroutines.flow.Flow

interface ProfileLocalDataSource {
    fun getProfile(): Flow<UserProfileEntity?>
    suspend fun saveProfile(profile: UserProfileEntity)
    suspend fun clearProfile()
}
