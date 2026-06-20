package com.ktx.dormitory.presentation.features.payment

import android.os.Parcelable
import com.ktx.dormitory.domain.model.Invoice
import kotlinx.parcelize.Parcelize

@Parcelize
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

sealed class PaymentUiEvent {
    object LoadInvoices : PaymentUiEvent()
    data class VerifyPayment(val invoiceId: String) : PaymentUiEvent()
}

sealed class PaymentUiEffect {
    data class ShowToast(val message: String) : PaymentUiEffect()
    object NavigateBack : PaymentUiEffect()
}
