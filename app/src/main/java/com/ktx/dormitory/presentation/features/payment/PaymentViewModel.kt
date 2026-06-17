package com.ktx.dormitory.presentation.features.payment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ktx.dormitory.domain.model.PaymentStatus
import com.ktx.dormitory.domain.repository.PaymentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val repository: PaymentRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<PaymentUiState>(PaymentUiState.Loading)
    val uiState: StateFlow<PaymentUiState> = _uiState.asStateFlow()

    init {
        loadInvoices()
    }

    fun loadInvoices() {
        viewModelScope.launch {
            _uiState.value = PaymentUiState.Loading
            repository.getInvoices()
                .onSuccess { data ->
                    val total = data.filter { it.status == PaymentStatus.UNPAID }.sumOf { it.amount }
                    _uiState.value = PaymentUiState.Success(data, total)
                }
                .onFailure {
                    _uiState.value = PaymentUiState.Error(it.message ?: "Lỗi tải hóa đơn")
                }
        }
    }

    fun verifyPayment(invoiceId: String) {
        viewModelScope.launch {
            // Hiển thị loading nhẹ hoặc giữ nguyên state cũ nhưng khóa UI
            repository.verifyPayment(invoiceId)
                .onSuccess {
                    // Tải lại danh sách để cập nhật trạng thái PAID
                    loadInvoices()
                }
                .onFailure {
                    // Có thể thông báo qua Toast hoặc Snackbar
                }
        }
    }
}
