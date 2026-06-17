package com.ktx.dormitory.core.network

import com.google.common.truth.Truth.assertThat
import com.ktx.dormitory.data.api.AuthApiService
import com.ktx.dormitory.data.local.TokenManager
import com.ktx.dormitory.domain.model.BaseResponse
import com.ktx.dormitory.domain.model.LoginResponse
import com.ktx.dormitory.domain.repository.UserRepository
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.coVerify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import org.junit.Before
import org.junit.Test
import javax.inject.Provider

class TokenAuthenticatorTest {

    private lateinit var authenticator: TokenAuthenticator
    private val tokenManager = mockk<TokenManager>(relaxed = true)
    private val userRepository = mockk<UserRepository>(relaxed = true)
    private val authApi = mockk<AuthApiService>()
    
    @Before
    fun setup() {
        authenticator = TokenAuthenticator(tokenManager, Provider { userRepository }, Provider { authApi })
        every { tokenManager.refreshToken } returns flowOf("old_refresh")
        every { tokenManager.accessToken } returns flowOf("old_access")
    }

    @Test
    fun `authenticate when 401 and refresh success returns new request`() = runTest {
        val request = Request.Builder().url("http://test.com").header("Authorization", "Bearer old_access").build()
        val response = Response.Builder().request(request).protocol(Protocol.HTTP_1_1).code(401).message("").build()
        
        coEvery { authApi.refreshToken(any()) } returns BaseResponse(true, "", LoginResponse("new_access", "new_refresh", "USER"))

        val result = authenticator.authenticate(null, response)

        assertThat(result?.header("Authorization")).isEqualTo("Bearer new_access")
    }

    @Test
    fun `authenticate when refresh fail triggers logout`() = runTest {
        val request = Request.Builder().url("http://test.com").header("Authorization", "Bearer old_access").build()
        val response = Response.Builder().request(request).protocol(Protocol.HTTP_1_1).code(401).message("").build()
        
        coEvery { authApi.refreshToken(any()) } returns BaseResponse(false, "Fail", null)

        val result = authenticator.authenticate(null, response)

        assertThat(result).isNull()
        coVerify { userRepository.clearAllData() }
    }
}
