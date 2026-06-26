package com.ktx.dormitory.domain.auth.usecase

import com.ktx.dormitory.domain.auth.model.UserData
import com.ktx.dormitory.domain.auth.repository.AuthRepository
import javax.inject.Inject

class GetAuthStateUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Result<UserData> {
        return authRepository.getCurrentUser()
    }
}
