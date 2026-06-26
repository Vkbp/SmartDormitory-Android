package com.ktx.dormitory.presentation.features.payment

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ktx.dormitory.domain.payment.model.PaymentStatus
import com.ktx.dormitory.domain.payment.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val getInvoicesUseCase: GetInvoicesUseCase,
    private val verifyPaymentUseCase: VerifyPaymentUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val uiState: StateFlow<PaymentUiState> = savedStateHandle.getStateFlow("uiState", PaymentUiState.Loading)

    private fun updateUiState(newState: PaymentUiState) {
        savedStateHandle["uiState"] = newState
    }

    init {
        loadInvoices()
    }

    fun loadInvoices() {
        viewModelScope.launch {
            updateUiState(PaymentUiState.Loading)
            getInvoicesUseCase()
                .onSuccess { list ->
                    val unpaid = list.filter { it.status != PaymentStatus.PAID }
                    val total = unpaid.sumOf { it.remainingAmount ?: it.amount ?: 0.0 }
                    updateUiState(PaymentUiState.Success(unpaid, total))
                }
                .onFailure { e ->
                    updateUiState(PaymentUiState.Error(e.message ?: "Lỗi tải hóa đơn"))
                }
        }
    }

    fun verifyPayment(billId: String, amount: Double) {
        viewModelScope.launch {
            // Simplified for demo, backend logic needed
            verifyPaymentUseCase(billId, amount, "VIETQR", "APP_CONFIRM_${System.currentTimeMillis()}")
                .onSuccess { loadInvoices() }
        }
    }
}
