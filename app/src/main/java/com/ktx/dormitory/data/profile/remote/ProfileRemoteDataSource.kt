package com.ktx.dormitory.data.profile.remote

import com.ktx.dormitory.data.common.dto.BaseResponse
import com.ktx.dormitory.data.common.dto.FileUploadResponse
import com.ktx.dormitory.data.profile.dto.*
import okhttp3.MultipartBody

interface ProfileRemoteDataSource {
    suspend fun getDetailedProfile(): BaseResponse<StudentResponse>
    suspend fun updateProfile(request: UpdateProfileRequest): BaseResponse<StudentResponse>
    suspend fun uploadAvatar(file: MultipartBody.Part): BaseResponse<FileUploadResponse>
}
