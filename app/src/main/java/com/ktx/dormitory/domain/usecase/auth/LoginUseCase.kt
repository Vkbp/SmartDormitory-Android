package com.ktx.dormitory.domain.usecase.auth

import com.ktx.dormitory.domain.model.UserData
import com.ktx.dormitory.domain.repository.AuthRepository
import com.ktx.dormitory.domain.repository.UserRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(usernameOrEmail: String, password: String): Result<UserData> {
        val result = authRepository.login(usernameOrEmail, password)
        if (result.isSuccess) {
            userRepository.saveLoginStatus(true)
        }
        return result
    }
}
