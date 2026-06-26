package com.ktx.dormitory.domain.payment.usecase

import com.ktx.dormitory.domain.payment.model.Transaction
import com.ktx.dormitory.domain.payment.repository.PaymentRepository
import javax.inject.Inject

class GetPaymentHistoryUseCase @Inject constructor(
    private val paymentRepository: PaymentRepository
) {
    suspend operator fun invoke(): Result<List<Transaction>> {
        return paymentRepository.getPaymentHistory()
    }
}
