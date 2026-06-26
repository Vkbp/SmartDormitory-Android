package com.ktx.dormitory.data.auth.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "token_prefs")

@Singleton
class TokenManager @Inject constructor(
    @ApplicationContext private val context: Context
) : AuthLocalDataSource {

    private val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

    private val sharedPreferences = EncryptedSharedPreferences.create(
        "secure_prefs",
        masterKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    private val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")

    override suspend fun saveTokens(accessToken: String, refreshToken: String) {
        sharedPreferences.edit().apply {
            putString("access_token", accessToken)
            putString("refresh_token", refreshToken)
            apply()
        }
    }

    override suspend fun clearTokens(keepRefreshToken: Boolean) {
        sharedPreferences.edit().apply {
            remove("access_token")
            if (!keepRefreshToken) remove("refresh_token")
            apply()
        }
    }

    override suspend fun getAccessTokenSync(): String? = sharedPreferences.getString("access_token", null)

    override suspend fun getRefreshTokenSync(): String? = sharedPreferences.getString("refresh_token", null)

    override suspend fun saveLoginStatus(isLoggedIn: Boolean) {
        context.dataStore.edit { it[IS_LOGGED_IN] = isLoggedIn }
    }

    override fun isLoggedIn(): Flow<Boolean> = context.dataStore.data.map {
        it[IS_LOGGED_IN] ?: false
    }
}
