package com.ktx.dormitory.domain.usecase.auth

import com.ktx.dormitory.domain.repository.AuthRepository
import javax.inject.Inject

class ForgotPasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String): Result<Unit> {
        if (!email.contains("@")) {
            return Result.failure(Exception("Email không hợp lệ"))
        }
        return authRepository.forgotPassword(email)
    }
}
