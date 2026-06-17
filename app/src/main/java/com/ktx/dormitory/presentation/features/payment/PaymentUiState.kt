package com.ktx.dormitory.presentation.features.payment

import com.ktx.dormitory.domain.model.Invoice

sealed class PaymentUiState {
    object Loading : PaymentUiState()
    data class Success(
        val invoices: List<Invoice>,
        val totalUnpaid: Double
    ) : PaymentUiState()
    data class Error(val message: String) : PaymentUiState()
}