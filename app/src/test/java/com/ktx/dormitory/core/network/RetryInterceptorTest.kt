package com.ktx.dormitory.core.network

import com.google.common.truth.Truth.assertThat
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test

class RetryInterceptorTest {

    private lateinit var server: MockWebServer
    private lateinit var client: OkHttpClient

    @Before
    fun setup() {
        server = MockWebServer()
        client = OkHttpClient.Builder()
            .addInterceptor(RetryInterceptor(maxRetry = 2))
            .build()
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun `should retry on 500 error`() {
        server.enqueue(MockResponse().setResponseCode(500))
        server.enqueue(MockResponse().setResponseCode(500))
        server.enqueue(MockResponse().setResponseCode(200))

        val request = Request.Builder()
            .url(server.url("/"))
            .get()
            .build()

        val response = client.newCall(request).execute()

        assertThat(response.isSuccessful).isTrue()
        assertThat(server.requestCount).isEqualTo(3)
    }

    @Test
    fun `should NOT retry on 400 error`() {
        server.enqueue(MockResponse().setResponseCode(400))
        server.enqueue(MockResponse().setResponseCode(200))

        val request = Request.Builder()
            .url(server.url("/"))
            .get()
            .build()

        val response = client.newCall(request).execute()

        assertThat(response.code).isEqualTo(400)
        assertThat(server.requestCount).isEqualTo(1)
    }
}
