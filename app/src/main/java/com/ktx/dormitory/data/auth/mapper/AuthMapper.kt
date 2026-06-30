package com.ktx.dormitory.data.auth.mapper

import com.ktx.dormitory.data.auth.dto.UserResponse
import com.ktx.dormitory.domain.auth.model.UserData

/**
 * Chuyển đổi DTO thành Domain Model cho Auth
 */
fun UserResponse.toDomain(): UserData {
    return UserData(
        id = this.id,
        username = this.username,
        role = this.role
    )
}
