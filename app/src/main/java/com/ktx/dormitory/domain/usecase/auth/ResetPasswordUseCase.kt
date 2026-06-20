package com.ktx.dormitory.domain.usecase.auth

import com.ktx.dormitory.domain.repository.AuthRepository
import javax.inject.Inject

class ResetPasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(token: String, newPass: String): Result<Unit> {
        if (newPass.length < 6) {
            return Result.failure(Exception("Mật khẩu mới phải có ít nhất 6 ký tự"))
        }
        return authRepository.resetPassword(token, newPass)
    }
}
