package com.ktx.dormitory.domain.auth.usecase

import com.ktx.dormitory.domain.auth.repository.AuthRepository
import javax.inject.Inject

class ChangePasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(oldPass: String, newPass: String): Result<Unit> {
        return authRepository.changePassword(oldPass, newPass)
    }
}
