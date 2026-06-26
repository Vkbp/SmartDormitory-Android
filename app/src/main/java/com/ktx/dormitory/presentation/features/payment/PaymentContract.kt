package com.ktx.dormitory.presentation.features.payment

import android.os.Parcelable
import com.ktx.dormitory.domain.payment.model.Invoice
import kotlinx.parcelize.Parcelize

sealed class PaymentUiState : Parcelable {
    @Parcelize
    data object Loading : PaymentUiState()
    
    @Parcelize
    data class Success(
        val invoices: List<Invoice>,
        val totalUnpaid: Double
    ) : PaymentUiState()
    
    @Parcelize
    data class Error(val message: String) : PaymentUiState()
}
