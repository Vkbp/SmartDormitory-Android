package com.ktx.dormitory.domain.auth.usecase

import com.ktx.dormitory.domain.auth.repository.AuthRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke() {
        authRepository.logout()
    }
}
