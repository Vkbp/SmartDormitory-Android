package com.ktx.dormitory.data.remote.dto.user

import com.google.gson.annotations.SerializedName

data class StudentResponse(
    @SerializedName("studentId") val id: String? = null,
    @SerializedName("studentCode") val studentCode: String? = null,
    @SerializedName("fullName") val fullName: String? = null,
    @SerializedName("cccd") val citizenId: String? = null,
    @SerializedName("email") val email: String? = null,
    @SerializedName("phone") val phone: String? = null,
    @SerializedName("faculty") val faculty: String? = null,
    @SerializedName("academicYear") val academicYear: String? = null,
    @SerializedName("fatherName") val fatherName: String? = null,
    @SerializedName("fatherPhone") val fatherPhone: String? = null,
    @SerializedName("motherName") val motherName: String? = null,
    @SerializedName("motherPhone") val motherPhone: String? = null,
    @SerializedName("emergencyContact") val emergencyContact: String? = null,
    @SerializedName("permanentAddress") val permanentAddress: String? = null,
    @SerializedName("avatarUrl") val avatarUrl: String? = null,
    @SerializedName("status") val status: String? = null,
    @SerializedName("gender") val gender: String? = null,
    @SerializedName("birthDate") val birthDate: String? = null,
    @SerializedName("course") val course: String? = null,
    @SerializedName("role") val role: String? = null
)
