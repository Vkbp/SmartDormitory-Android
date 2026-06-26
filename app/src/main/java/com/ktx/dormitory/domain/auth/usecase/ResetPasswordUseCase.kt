package com.ktx.dormitory.domain.auth.usecase

import com.ktx.dormitory.domain.auth.repository.AuthRepository
import javax.inject.Inject

class ResetPasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(token: String, newPass: String): Result<Unit> {
        return authRepository.resetPassword(token, newPass)
    }
}
