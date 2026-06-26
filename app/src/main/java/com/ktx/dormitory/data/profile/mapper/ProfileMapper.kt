package com.ktx.dormitory.data.profile.mapper

import com.ktx.dormitory.data.local.entity.UserProfileEntity
import com.ktx.dormitory.data.remote.dto.user.StudentResponse
import com.ktx.dormitory.data.remote.dto.user.UpdateProfileRequest as UpdateProfileDto
import com.ktx.dormitory.domain.profile.model.UpdateProfileRequest
import com.ktx.dormitory.domain.profile.model.UserProfile

fun StudentResponse.toDomain(): UserProfile {
    return UserProfile(
        id = id,
        studentCode = studentCode,
        fullName = fullName,
        citizenId = citizenId,
        email = email,
        phone = phone,
        faculty = faculty,
        academicYear = academicYear,
        fatherName = fatherName,
        fatherPhone = fatherPhone,
        motherName = motherName,
        motherPhone = motherPhone,
        emergencyContact = emergencyContact,
        permanentAddress = permanentAddress,
        avatarUrl = avatarUrl,
        status = status,
        gender = gender,
        birthDate = birthDate,
        course = course
    )
}

fun StudentResponse.toEntity(): UserProfileEntity {
    return UserProfileEntity(
        id = id ?: "",
        studentCode = studentCode ?: "",
        fullName = fullName ?: "",
        cccd = citizenId ?: "",
        email = email ?: "",
        phone = phone ?: "",
        faculty = faculty ?: "",
        academicYear = academicYear ?: "",
        permanentAddress = permanentAddress ?: "",
        avatarUrl = avatarUrl,
        fatherName = fatherName,
        fatherPhone = fatherPhone,
        motherName = motherName,
        motherPhone = motherPhone,
        emergencyContact = emergencyContact,
        status = status
    )
}

fun UserProfileEntity.toDomain(): UserProfile {
    return UserProfile(
        id = id,
        studentCode = studentCode,
        fullName = fullName,
        citizenId = cccd,
        email = email,
        phone = phone,
        faculty = faculty,
        academicYear = academicYear,
        permanentAddress = permanentAddress,
        avatarUrl = avatarUrl,
        fatherName = fatherName,
        fatherPhone = fatherPhone,
        motherName = motherName,
        motherPhone = motherPhone,
        emergencyContact = emergencyContact,
        status = status
    )
}

fun UpdateProfileRequest.toDto(): UpdateProfileDto {
    return UpdateProfileDto(
        email = email,
        phone = phone,
        fatherName = fatherName,
        fatherPhone = fatherPhone,
        motherName = motherName,
        motherPhone = motherPhone,
        emergencyContact = emergencyContact,
        permanentAddress = permanentAddress,
        avatarUrl = avatarUrl
    )
}
