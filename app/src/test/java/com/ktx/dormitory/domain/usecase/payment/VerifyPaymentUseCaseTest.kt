package com.ktx.dormitory.domain.usecase.payment

import com.google.common.truth.Truth.assertThat
import com.ktx.dormitory.domain.repository.PaymentRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class VerifyPaymentUseCaseTest {

    private val repository = mockk<PaymentRepository>()
    private val useCase = VerifyPaymentUseCase(repository)

    @Test
    fun `invoke success returns success`() = runTest {
        coEvery { repository.verifyPayment("123") } returns Result.success(Unit)

        val result = useCase("123")

        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `invoke failure returns failure`() = runTest {
        coEvery { repository.verifyPayment("123") } returns Result.failure(Exception("Failed"))

        val result = useCase("123")

        assertThat(result.isFailure).isTrue()
    }
}
