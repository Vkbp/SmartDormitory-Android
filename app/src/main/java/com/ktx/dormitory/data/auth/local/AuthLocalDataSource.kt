package com.ktx.dormitory.data.auth.local

import kotlinx.coroutines.flow.Flow

interface AuthLocalDataSource {
    suspend fun saveTokens(accessToken: String, refreshToken: String)
    suspend fun clearTokens(keepRefreshToken: Boolean = false)
    suspend fun getAccessTokenSync(): String?
    suspend fun getRefreshTokenSync(): String?
    
    suspend fun saveLoginStatus(isLoggedIn: Boolean)
    fun isLoggedIn(): Flow<Boolean>
}
