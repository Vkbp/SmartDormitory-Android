package com.ktx.dormitory.domain.repository

import com.ktx.dormitory.domain.model.*

/**
 * Interface quản lý các tính năng nghiệp vụ người dùng.
 * getMyBills() đã bị xóa - hóa đơn lấy từ PaymentRepository.
 */
interface UserRepository {
    suspend fun getProfile(): Result<UserProfile>
    suspend fun updateProfile(request: UpdateProfileRequest): Result<Unit>
    suspend fun getRoomInfo(): Result<RoomInfo>
    suspend fun getApplicationTimeline(): Result<DormApplication>
    suspend fun getPaymentHistory(): Result<List<Transaction>>
    suspend fun uploadAvatar(filePath: String): Result<String>
    suspend fun saveLoginStatus(isLoggedIn: Boolean)
    suspend fun clearAllData()
}
