package com.ktx.dormitory.core.network

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * Interceptor tự động thử lại request khi gặp lỗi mạng (IOException)
 * Sử dụng chiến lược Exponential Backoff đơn giản.
 */
class RetryInterceptor(
    private val maxRetry: Int = 3
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        var response = try {
            chain.proceed(request)
        } catch (e: IOException) {
            null
        }

        var tryCount = 0
        while ((response == null || !response.isSuccessful) && tryCount < maxRetry) {
            // Chỉ retry với các lỗi server (5xx) hoặc lỗi kết nối (IOException)
            // Không retry với lỗi client (4xx)
            if (response != null && response.code < 500) {
                break
            }

            tryCount++
            
            // Đợi trước khi thử lại (Exponential Backoff: 1s, 2s, 4s)
            val waitTime = Math.pow(2.0, (tryCount - 1).toDouble()).toLong() * 1000
            try {
                Thread.sleep(waitTime)
            } catch (e: InterruptedException) {
                break
            }

            response?.close()
            response = try {
                chain.proceed(request)
            } catch (e: IOException) {
                null
            }
        }

        return response ?: chain.proceed(request) // Nếu vẫn lỗi thì trả về kết quả cuối cùng
    }
}
