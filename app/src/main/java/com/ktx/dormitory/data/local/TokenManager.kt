package com.ktx.dormitory.data.local

import android.content.SharedPreferences
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenManager @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {
    companion object {
        private const val ACCESS_TOKEN_KEY = "access_token"
        private const val REFRESH_TOKEN_KEY = "refresh_token"
        private const val IS_PAID_KEY = "is_paid"
    }

    // Biến phụ trợ để biến SharedPreferences (Blocking) thành Flow (Reactive)
    // Giúp bạn không phải sửa code ở UI hay ViewModel
    private fun <T> getFlowForKey(key: String, defaultValue: T): Flow<T> = callbackFlow {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { prefs, changedKey ->
            if (key == changedKey) {
                @Suppress("UNCHECKED_CAST")
                val value = when (defaultValue) {
                    is String -> prefs.getString(key, defaultValue) as T
                    is Boolean -> prefs.getBoolean(key, defaultValue) as T
                    else -> defaultValue
                }
                trySend(value ?: defaultValue)
            }
        }
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)

        // Phát giá trị khởi tạo
        @Suppress("UNCHECKED_CAST")
        val initialValue = when (defaultValue) {
            is String -> sharedPreferences.getString(key, defaultValue) as T
            is Boolean -> sharedPreferences.getBoolean(key, defaultValue) as T
            else -> defaultValue
        }
        trySend(initialValue ?: defaultValue)

        awaitClose { sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener) }
    }

    // Các biến Flow dùng cho UI/Authenticator (Giữ nguyên tên cũ)
    val accessToken: Flow<String?> = getFlowForKey(ACCESS_TOKEN_KEY, "")
    val refreshToken: Flow<String?> = getFlowForKey(REFRESH_TOKEN_KEY, "")
    val isPaid: Flow<Boolean> = getFlowForKey(IS_PAID_KEY, false)

    // Synchronous access for Interceptors (Performance improvement)
    fun getAccessTokenSync(): String? = sharedPreferences.getString(ACCESS_TOKEN_KEY, "")
    fun getRefreshTokenSync(): String? = sharedPreferences.getString(REFRESH_TOKEN_KEY, "")

    // Lưu Token bảo mật
    fun saveTokens(access: String, refresh: String) {
        sharedPreferences.edit().apply {
            putString(ACCESS_TOKEN_KEY, access)
            putString(REFRESH_TOKEN_KEY, refresh)
            apply()
        }
    }

    // Lưu trạng thái thanh toán bảo mật
    fun savePaymentStatus(paid: Boolean) {
        sharedPreferences.edit().putBoolean(IS_PAID_KEY, paid).apply()
    }

    // Xóa sạch dữ liệu (Logout)
    fun clearTokens(keepRefreshToken: Boolean = false) {
        sharedPreferences.edit().apply {
            remove(ACCESS_TOKEN_KEY)
            if (!keepRefreshToken) {
                remove(REFRESH_TOKEN_KEY)
            }
            apply()
        }
    }
}