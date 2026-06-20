package com.ktx.dormitory.domain.usecase.payment

import com.ktx.dormitory.domain.model.Invoice
import com.ktx.dormitory.domain.repository.PaymentRepository
import javax.inject.Inject

class GetInvoicesUseCase @Inject constructor(
    private val paymentRepository: PaymentRepository
) {
    suspend operator fun invoke(): Result<List<Invoice>> {
        return paymentRepository.getInvoices()
    }
}
