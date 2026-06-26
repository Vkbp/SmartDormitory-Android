package com.ktx.dormitory.core.network

import com.ktx.dormitory.data.auth.local.TokenManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
        
        // Không thêm token cho các endpoint Auth công khai
        val path = chain.request().url.encodedPath
        if (!path.contains("/auth/login") && !path.contains("/auth/refresh")) {
            val token = tokenManager.getAccessTokenSync()
            if (!token.isNullOrBlank()) {
                requestBuilder.addHeader("Authorization", "Bearer $token")
            }
        }

        return chain.proceed(requestBuilder.build())
    }
}
