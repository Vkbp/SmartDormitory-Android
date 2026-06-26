package com.ktx.dormitory.domain.payment.usecase

import com.ktx.dormitory.domain.payment.model.Invoice
import com.ktx.dormitory.domain.payment.repository.PaymentRepository
import javax.inject.Inject

class GetInvoicesUseCase @Inject constructor(
    private val paymentRepository: PaymentRepository
) {
    suspend operator fun invoke(): Result<List<Invoice>> {
        return paymentRepository.getInvoices()
    }
}
