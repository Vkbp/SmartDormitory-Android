package com.ktx.dormitory.domain.usecase.user

import com.ktx.dormitory.domain.model.Transaction
import com.ktx.dormitory.domain.repository.UserRepository
import javax.inject.Inject

class GetPaymentHistoryUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(): Result<List<Transaction>> {
        return repository.getPaymentHistory()
    }
}
