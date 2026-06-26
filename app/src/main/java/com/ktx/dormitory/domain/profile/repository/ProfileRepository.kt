package com.ktx.dormitory.domain.profile.repository

import com.ktx.dormitory.domain.profile.model.UpdateProfileRequest
import com.ktx.dormitory.domain.profile.model.UserProfile

interface ProfileRepository {
    suspend fun getProfile(): Result<UserProfile>
    suspend fun updateProfile(request: UpdateProfileRequest): Result<Unit>
    suspend fun uploadAvatar(filePath: String): Result<String>
}
