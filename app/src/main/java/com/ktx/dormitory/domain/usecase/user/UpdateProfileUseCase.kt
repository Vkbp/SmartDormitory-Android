package com.ktx.dormitory.domain.usecase.user

import com.ktx.dormitory.domain.model.UpdateProfileRequest
import com.ktx.dormitory.domain.repository.UserRepository
import javax.inject.Inject

class UpdateProfileUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(fullName: String, phone: String, email: String): Result<Unit> {
        if (fullName.isBlank()) return Result.failure(Exception("Họ tên không được để trống"))
        val request = UpdateProfileRequest(fullName = fullName, phone = phone, email = email)
        return userRepository.updateProfile(request)
    }
}
