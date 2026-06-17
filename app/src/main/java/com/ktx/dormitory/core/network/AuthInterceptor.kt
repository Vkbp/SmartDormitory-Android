package com.ktx.dormitory.core.network

import com.ktx.dormitory.data.local.TokenManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val path = request.url.encodedPath
        val requestBuilder = request.newBuilder()

        val isPublicApi = path.contains("auth/login", ignoreCase = true) || 
                         path.contains("auth/refresh-token", ignoreCase = true) ||
                         path.contains("auth/forgot-password", ignoreCase = true)

        if (!isPublicApi) {
            // Loại bỏ runBlocking, sử dụng gọi trực tiếp từ SharedPreferences (Synchronous)
            // giúp tăng hiệu năng và ổn định luồng OkHttp
            val token = tokenManager.getAccessTokenSync()
            if (!token.isNullOrEmpty()) {
                requestBuilder.addHeader("Authorization", "Bearer $token")
            }
        }

        return chain.proceed(requestBuilder.build())
    }
}
