package com.ktx.dormitory.core.network

import com.ktx.dormitory.data.local.TokenManager
import com.ktx.dormitory.data.remote.api.AuthApiService
import com.ktx.dormitory.data.remote.dto.auth.RefreshTokenRequest
import com.ktx.dormitory.domain.repository.UserRepository
import com.ktx.dormitory.util.AuthEvent
import com.ktx.dormitory.util.AuthEventBus
import kotlinx.coroutines.flow.first
import com.ktx.dormitory.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject
import javax.inject.Provider

class TokenAuthenticator @Inject constructor(
    private val tokenManager: TokenManager,
    private val userRepositoryProvider: Provider<UserRepository>,
    private val apiHolder: Provider<AuthApiService>,
    @ApplicationScope private val applicationScope: CoroutineScope
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        // Chống lặp quá 2 lần
        if (responseCount(response) >= 3) {
            handleLogout()
            return null
        }

        if (response.code == 401) {
            val refreshToken = tokenManager.getRefreshTokenSync()

            if (refreshToken.isNullOrEmpty() || response.request.url.encodedPath.contains("auth/refresh-token")) {
                handleLogout()
                return null
            }

            // Đồng bộ hóa việc refresh
            synchronized(this) {
                val currentToken = tokenManager.getAccessTokenSync()
                
                // Nếu token đã được refresh bởi request khác, thử lại ngay
                val requestToken = response.request.header("Authorization")?.replace("Bearer ", "")
                if (currentToken != requestToken && !currentToken.isNullOrEmpty()) {
                    return response.request.newBuilder()
                        .header("Authorization", "Bearer $currentToken")
                        .build()
                }

                return try {
                    // Refresh token là IO operation, vẫn cần runBlocking trong Authenticator 
                    // nhưng đã giảm thiểu việc đọc Flow không cần thiết.
                    val refreshRes = runBlocking {
                        apiHolder.get().refreshToken(RefreshTokenRequest(refreshToken))
                    }

                    if (refreshRes.success && refreshRes.data != null) {
                        val newData = refreshRes.data
                        tokenManager.saveTokens(newData.accessToken, newData.refreshToken)

                        response.request.newBuilder()
                            .header("Authorization", "Bearer ${newData.accessToken}")
                            .build()
                    } else {
                        handleLogout()
                        null
                    }
                } catch (e: Exception) {
                    handleLogout()
                    null
                }
            }
        }
        return null
    }

    private fun responseCount(response: Response): Int {
        var result = 1
        var priorResponse = response.priorResponse
        while (priorResponse != null) {
            result++
            priorResponse = priorResponse.priorResponse
        }
        return result
    }

    private fun handleLogout() {
        applicationScope.launch {
            try {
                tokenManager.clearTokens()
                userRepositoryProvider.get().clearAllData()
                AuthEventBus.emit(AuthEvent.LOGOUT)
            } catch (e: Exception) { /* Ignore */ }
        }
    }
}
