package com.ktx.dormitory.data.local.datasource

import com.ktx.dormitory.data.local.entity.UserProfileEntity
import kotlinx.coroutines.flow.Flow

interface UserLocalDataSource {
    suspend fun saveLoginStatus(isLoggedIn: Boolean)
    fun isLoggedIn(): Flow<Boolean>
    
    fun getProfile(): Flow<UserProfileEntity?>
    suspend fun saveProfile(profile: UserProfileEntity)

    suspend fun clearAllData()
}
