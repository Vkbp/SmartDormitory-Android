package com.ktx.dormitory.presentation.features.payment

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.ktx.dormitory.data.repository.MockDataProvider
import com.ktx.dormitory.domain.model.PaymentStatus
import com.ktx.dormitory.domain.repository.PaymentRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PaymentViewModelTest {

    private lateinit var viewModel: PaymentViewModel
    private val repository = mockk<PaymentRepository>()
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        // Default success behavior for init
        coEvery { repository.getInvoices() } returns Result.success(MockDataProvider.getMockInvoices())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadInvoices success updates state with invoices and correct total`() = runTest {
        val mockInvoices = MockDataProvider.getMockInvoices()
        val expectedTotal = mockInvoices.filter { it.status == PaymentStatus.UNPAID }.sumOf { it.amount }

        coEvery { repository.getInvoices() } coAnswers {
            delay(1) // Force suspension to capture loading state
            Result.success(mockInvoices)
        }

        viewModel = PaymentViewModel(repository)

        viewModel.uiState.test {
            // 1. Loading state
            assertThat(awaitItem()).isEqualTo(PaymentUiState.Loading)
            
            // 2. Success state
            val successState = awaitItem() as PaymentUiState.Success
            assertThat(successState.invoices).isEqualTo(mockInvoices)
            assertThat(successState.totalUnpaid).isEqualTo(expectedTotal)
            
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `loadInvoices failure updates state with error message`() = runTest {
        val errorMsg = "Network Error"
        coEvery { repository.getInvoices() } coAnswers {
            delay(1)
            Result.failure(Exception(errorMsg))
        }

        viewModel = PaymentViewModel(repository)

        viewModel.uiState.test {
            assertThat(awaitItem()).isEqualTo(PaymentUiState.Loading)
            
            val errorState = awaitItem() as PaymentUiState.Error
            assertThat(errorState.message).isEqualTo(errorMsg)
            
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `loadInvoices empty list updates state with zero total`() = runTest {
        coEvery { repository.getInvoices() } coAnswers {
            delay(1)
            Result.success(emptyList())
        }

        viewModel = PaymentViewModel(repository)

        viewModel.uiState.test {
            assertThat(awaitItem()).isEqualTo(PaymentUiState.Loading)
            
            val successState = awaitItem() as PaymentUiState.Success
            assertThat(successState.invoices).isEmpty()
            assertThat(successState.totalUnpaid).isEqualTo(0.0)
            
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `verifyPayment success reloads invoices`() = runTest {
        viewModel = PaymentViewModel(repository)
        
        coEvery { repository.verifyPayment(any()) } returns Result.success(Unit)
        // Ensure subsequent call returns updated data (or same data for simplicity of reload check)
        coEvery { repository.getInvoices() } returns Result.success(MockDataProvider.getMockInvoices())

        viewModel.verifyPayment("invoice_id")
        
        // Check if getInvoices was called again (twice: once in init, once after verify)
        io.mockk.coVerify(exactly = 2) { repository.getInvoices() }
    }
}
