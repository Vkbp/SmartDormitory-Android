package com.ktx.dormitory.data.mapper

import com.ktx.dormitory.data.remote.dto.auth.UserResponse
import com.ktx.dormitory.domain.model.UserData

/**
 * Chuyển đổi DTO thành Domain Model cho Auth
 */
fun UserResponse.toDomain(): UserData {
    return UserData(
        id = this.id,
        username = this.username,
        role = this.role,
        fullName = this.fullName
    )
}
