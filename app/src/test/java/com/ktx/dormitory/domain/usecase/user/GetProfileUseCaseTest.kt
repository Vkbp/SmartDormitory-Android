package com.ktx.dormitory.domain.usecase.user

import com.google.common.truth.Truth.assertThat
import com.ktx.dormitory.domain.model.UserProfile
import com.ktx.dormitory.domain.repository.UserRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class GetProfileUseCaseTest {

    private val repository = mockk<UserRepository>()
    private val useCase = GetProfileUseCase(repository)

    @Test
    fun `invoke should return profile from repository`() = runTest {
        val mockProfile = mockk<UserProfile>()
        coEvery { repository.getProfile() } returns Result.success(mockProfile)

        val result = useCase()

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(mockProfile)
    }

    @Test
    fun `invoke failure should return error result`() = runTest {
        coEvery { repository.getProfile() } returns Result.failure(Exception("Network Error"))

        val result = useCase()

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()?.message).isEqualTo("Network Error")
    }
}
