package com.ktx.dormitory.presentation.features.payment

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ktx.dormitory.domain.model.PaymentStatus
import com.ktx.dormitory.domain.usecase.payment.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val getInvoicesUseCase: GetInvoicesUseCase,
    private val verifyPaymentUseCase: VerifyPaymentUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val uiState: StateFlow<PaymentUiState> = savedStateHandle.getStateFlow("uiState", PaymentUiState.Loading)

    val errorEvent: StateFlow<String?> = savedStateHandle.getStateFlow("errorEvent", null)

    private fun updateUiState(state: PaymentUiState) {
        savedStateHandle["uiState"] = state
    }

    private fun setErrorEvent(error: String?) {
        savedStateHandle["errorEvent"] = error
    }

    private val actionMutex = Mutex()

    init {
        if (uiState.value is PaymentUiState.Loading) {
            loadInvoices()
        }
    }

    fun loadInvoices() {
        viewModelScope.launch {
            updateUiState(PaymentUiState.Loading)
            getInvoicesUseCase()
                .onSuccess { data ->
                    // Backend Map từ /v1/bills KHÔNG có remainingAmount, dùng amount làm fallback
                    val total = data.filter { (it.status == PaymentStatus.UNPAID) || (it.status == PaymentStatus.PARTIALLY_PAID) }
                        .sumOf { it.remainingAmount ?: it.amount ?: 0.0 }
                    updateUiState(PaymentUiState.Success(data, total))
                }
                .onFailure {
                    updateUiState(PaymentUiState.Error(it.message ?: "Lỗi tải hóa đơn"))
                }
        }
    }

    /**
     * Xác nhận đã chuyển khoản ngân hàng cho hóa đơn.
     * Gửi POST v1/payments/online với phương thức BANK_TRANSFER.
     * transactionCode được tạo tự động từ billId + timestamp.
     *
     * @param billId UUID của hóa đơn cần thanh toán
     * @param amount Số tiền thanh toán
     */
    fun verifyPayment(billId: String, amount: Double) {
        viewModelScope.launch {
            // Ngăn chặn spam click bằng Mutex
            if (actionMutex.isLocked) return@launch

            actionMutex.withLock {
                val transactionCode = "PAY_${billId.take(8)}_${System.currentTimeMillis()}"
                verifyPaymentUseCase(
                    billId = billId,
                    amount = amount,
                    paymentMethod = "BANK_TRANSFER",
                    transactionCode = transactionCode
                )
                    .onSuccess {
                        loadInvoices()
                    }
                    .onFailure {
                        setErrorEvent(it.message ?: "Xác thực thanh toán thất bại")
                    }
            }
        }
    }

    fun clearError() {
        setErrorEvent(null)
    }
}
