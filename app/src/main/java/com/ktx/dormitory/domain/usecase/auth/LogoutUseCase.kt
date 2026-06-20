package com.ktx.dormitory.domain.usecase.auth

import com.ktx.dormitory.domain.repository.AuthRepository
import com.ktx.dormitory.domain.repository.UserRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke() {
        authRepository.logout()
        userRepository.saveLoginStatus(false)
        userRepository.clearAllData()
    }
}
