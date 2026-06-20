package com.ktx.dormitory.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: String,
    val studentCode: String,
    val fullName: String,
    val citizenId: String,
    val gender: String,
    val birthDate: String,
    val faculty: String,
    val course: String,
    val phone: String,
    val email: String,
    val permanentAddress: String,
    val role: String,
    val avatarUrl: String?
)
