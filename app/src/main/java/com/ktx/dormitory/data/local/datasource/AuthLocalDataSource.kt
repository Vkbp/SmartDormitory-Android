package com.ktx.dormitory.data.local.datasource

import kotlinx.coroutines.flow.Flow

interface AuthLocalDataSource {
    val accessToken: Flow<String?>
    val refreshToken: Flow<String?>
    val isPaid: Flow<Boolean>
    
    fun getAccessTokenSync(): String?
    fun getRefreshTokenSync(): String?
    
    fun saveTokens(access: String, refresh: String)
    fun savePaymentStatus(paid: Boolean)
    fun clearTokens(keepRefreshToken: Boolean = false)
}
