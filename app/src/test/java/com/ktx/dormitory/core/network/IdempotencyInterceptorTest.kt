package com.ktx.dormitory.core.network

import com.google.common.truth.Truth.assertThat
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test

class IdempotencyInterceptorTest {

    private lateinit var server: MockWebServer
    private lateinit var client: OkHttpClient

    @Before
    fun setup() {
        server = MockWebServer()
        client = OkHttpClient.Builder()
            .addInterceptor(IdempotencyInterceptor())
            .build()
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun `POST request should have X-Idempotency-Key header`() {
        server.enqueue(MockResponse().setResponseCode(200))

        val request = Request.Builder()
            .url(server.url("/"))
            .post("".toRequestBody("text/plain".toMediaType()))
            .build()

        client.newCall(request).execute()

        val recordedRequest = server.takeRequest()
        assertThat(recordedRequest.getHeader("X-Idempotency-Key")).isNotNull()
    }

    @Test
    fun `GET request should NOT have X-Idempotency-Key header`() {
        server.enqueue(MockResponse().setResponseCode(200))

        val request = Request.Builder()
            .url(server.url("/"))
            .get()
            .build()

        client.newCall(request).execute()

        val recordedRequest = server.takeRequest()
        assertThat(recordedRequest.getHeader("X-Idempotency-Key")).isNull()
    }
}
