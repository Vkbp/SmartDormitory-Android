package com.ktx.dormitory.domain.auth.usecase

import com.ktx.dormitory.domain.auth.repository.AuthRepository
import javax.inject.Inject

class ForgotPasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String): Result<Unit> {
        return authRepository.forgotPassword(email)
    }
}
