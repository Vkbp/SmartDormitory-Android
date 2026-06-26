package com.ktx.dormitory.domain.access.usecase

import com.ktx.dormitory.domain.access.model.AccessLog
import com.ktx.dormitory.domain.access.repository.AccessRepository
import javax.inject.Inject

class GetAccessHistoryUseCase @Inject constructor(
    private val repository: AccessRepository
) {
    suspend operator fun invoke(studentId: String): Result<List<AccessLog>> {
        return repository.getAccessHistory(studentId)
    }
}
