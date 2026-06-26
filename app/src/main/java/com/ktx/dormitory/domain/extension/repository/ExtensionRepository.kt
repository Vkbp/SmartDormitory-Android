package com.ktx.dormitory.domain.extension.repository

import com.ktx.dormitory.domain.extension.model.ExtensionRequest

interface ExtensionRepository {
    suspend fun requestExtension(studentId: String, reason: String): Result<Unit>
}
