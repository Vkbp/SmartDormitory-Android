package com.ktx.dormitory.domain.usecase.auth

import com.ktx.dormitory.domain.model.UserData
import com.ktx.dormitory.domain.repository.AuthRepository
import javax.inject.Inject

class GetAuthStateUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Result<UserData> {
        return authRepository.getCurrentUser()
    }
}
