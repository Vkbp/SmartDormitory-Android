package com.ktx.dormitory.core.network

import com.ktx.dormitory.data.auth.local.TokenManager
import com.ktx.dormitory.data.remote.api.AuthApiService
import com.ktx.dormitory.data.remote.dto.auth.RefreshTokenRequest
import com.ktx.dormitory.domain.auth.repository.AuthRepository
import dagger.Lazy
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject
import javax.inject.Provider

class TokenAuthenticator @Inject constructor(
    private val tokenManager: TokenManager,
    private val authApiProvider: Provider<AuthApiService>,
    private val authRepositoryLazy: Lazy<AuthRepository>
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        // Tránh loop vô tận
        if (response.countPriorResponse() >= 2) return null

        val refreshToken = tokenManager.getRefreshTokenSync() ?: return null

        synchronized(this) {
            val currentToken = tokenManager.getAccessTokenSync()
            val requestToken = response.request.header("Authorization")?.removePrefix("Bearer ")

            // Nếu token trong request khác với token hiện tại (nghĩa là đã được refresh bởi thread khác)
            if (requestToken != currentToken) {
                return response.request.newBuilder()
                    .header("Authorization", "Bearer $currentToken")
                    .build()
            }

            // Thực hiện refresh token
            return runBlocking {
                try {
                    val api = authApiProvider.get()
                    val refreshResponse = api.refreshToken(RefreshTokenRequest(refreshToken))
                    
                    if (refreshResponse.success && refreshResponse.data != null) {
                        val newData = refreshResponse.data
                        tokenManager.saveTokens(newData.accessToken, newData.refreshToken)
                        
                        response.request.newBuilder()
                            .header("Authorization", "Bearer ${newData.accessToken}")
                            .build()
                    } else {
                        handleAuthFailure()
                        null
                    }
                } catch (e: Exception) {
                    handleAuthFailure()
                    null
                }
            }
        }
    }

    private fun handleAuthFailure() {
        runBlocking {
            authRepositoryLazy.get().logout()
        }
    }

    private fun Response.countPriorResponse(): Int {
        var result = 0
        var prior = priorResponse
        while (result < 5 && prior != null) {
            result++
            prior = prior.priorResponse
        }
        return result
    }
}
