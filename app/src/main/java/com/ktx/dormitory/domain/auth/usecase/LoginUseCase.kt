package com.ktx.dormitory.domain.auth.usecase

import com.ktx.dormitory.domain.auth.model.UserData
import com.ktx.dormitory.domain.auth.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(usernameOrEmail: String, password: String): Result<UserData> {
        val result = authRepository.login(usernameOrEmail, password)
        if (result.isSuccess) {
            authRepository.saveLoginStatus(true)
        }
        return result
    }
}
