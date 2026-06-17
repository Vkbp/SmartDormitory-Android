package com.ktx.dormitory.data.repository

import com.google.common.truth.Truth.assertThat
import com.ktx.dormitory.data.api.AuthApiService
import com.ktx.dormitory.data.local.TokenManager
import com.ktx.dormitory.domain.model.BaseResponse
import com.ktx.dormitory.domain.model.LoginResponse
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class AuthRepositoryTest {

    private lateinit var repository: AuthRepositoryImpl
    private val api = mockk<AuthApiService>()
    private val tokenManager = mockk<TokenManager>(relaxed = true)

    @Before
    fun setup() {
        repository = AuthRepositoryImpl(api, tokenManager)
    }

    @Test
    fun `login success saves tokens and returns success result`() = runTest {
        val username = "user"
        val password = "password"
        val loginData = LoginResponse("access", "refresh", "USER")
        val response = BaseResponse(success = true, message = "Success", data = loginData)

        coEvery { api.login(any()) } returns response

        val result = repository.login(username, password)

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(loginData)
        
        // Verify token storage
        verify { tokenManager.saveTokens("access", "refresh") }
        verify { tokenManager.clearTokens() } // Should clear before login
    }

    @Test
    fun `login failure returns failure result`() = runTest {
        val response = BaseResponse<LoginResponse>(success = false, message = "Error", data = null)
        coEvery { api.login(any()) } returns response

        val result = repository.login("user", "pass")

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()?.message).isEqualTo("Error")
    }

    @Test
    fun `logout calls api and clears tokens`() = runTest {
        coEvery { api.logout() } returns BaseResponse(true, "", Unit)
        
        repository.logout()
        
        coVerify { api.logout() }
        verify { tokenManager.clearTokens(keepRefreshToken = true) }
    }
}
