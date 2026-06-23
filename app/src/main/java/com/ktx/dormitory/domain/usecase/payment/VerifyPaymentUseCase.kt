package com.ktx.dormitory.domain.usecase.payment

import com.ktx.dormitory.domain.repository.PaymentRepository
import javax.inject.Inject

class VerifyPaymentUseCase @Inject constructor(
    private val paymentRepository: PaymentRepository
) {
    suspend operator fun invoke(
        billId: String,
        amount: Double,
        paymentMethod: String,
        transactionCode: String
    ): Result<Unit> {
        return paymentRepository.verifyPayment(billId, amount, paymentMethod, transactionCode)
    }
}
