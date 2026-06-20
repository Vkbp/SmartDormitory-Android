package com.ktx.dormitory.presentation.features.payment

import androidx.lifecycle.SavedStateHandle
import com.google.common.truth.Truth.assertThat
import com.ktx.dormitory.domain.model.Invoice
import com.ktx.dormitory.domain.model.PaymentStatus
import com.ktx.dormitory.domain.usecase.payment.GetInvoicesUseCase
import com.ktx.dormitory.domain.usecase.payment.VerifyPaymentUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PaymentViewModelTest {

    private lateinit var viewModel: PaymentViewModel
    private val getInvoicesUseCase = mockk<GetInvoicesUseCase>()
    private val verifyPaymentUseCase = mockk<VerifyPaymentUseCase>()
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        coEvery { getInvoicesUseCase() } returns Result.success(emptyList())
        viewModel = PaymentViewModel(getInvoicesUseCase, verifyPaymentUseCase, SavedStateHandle())
        testDispatcher.scheduler.advanceUntilIdle()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadInvoices success updates state with total amount`() = runTest(testDispatcher) {
        val invoices = listOf(
            Invoice(1, null, 100000.0, 0.0, 100000.0, PaymentStatus.UNPAID, "2023-12-01", "Dien", null, null),
            Invoice(2, null, 50000.0, 50000.0, 0.0, PaymentStatus.PAID, "2023-12-01", "Nuoc", null, null)
        )
        coEvery { getInvoicesUseCase() } returns Result.success(invoices)

        viewModel.loadInvoices()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertThat(state).isInstanceOf(PaymentUiState.Success::class.java)
        val successState = state as PaymentUiState.Success
        assertThat(successState.invoices).hasSize(2)
        assertThat(successState.totalUnpaid).isEqualTo(100000.0)
    }

    @Test
    fun `verifyPayment success reloads invoices`() = runTest(testDispatcher) {
        coEvery { verifyPaymentUseCase(any()) } returns Result.success(Unit)
        coEvery { getInvoicesUseCase() } returns Result.success(emptyList())

        viewModel.verifyPayment("1")
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertThat(state).isInstanceOf(PaymentUiState.Success::class.java)
    }
}
