package com.ktx.dormitory.domain.extension.usecase

import com.ktx.dormitory.domain.extension.repository.ExtensionRepository
import javax.inject.Inject

class RequestExtensionUseCase @Inject constructor(
    private val repository: ExtensionRepository
) {
    suspend operator fun invoke(studentId: String, reason: String): Result<Unit> {
        return repository.requestExtension(studentId, reason)
    }
}
