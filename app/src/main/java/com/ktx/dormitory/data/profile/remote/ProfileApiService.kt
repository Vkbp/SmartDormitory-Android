package com.ktx.dormitory.data.profile.remote

import com.ktx.dormitory.data.common.dto.BaseResponse
import com.ktx.dormitory.data.common.dto.FileUploadResponse
import com.ktx.dormitory.data.profile.dto.*
import okhttp3.MultipartBody
import retrofit2.http.*

interface ProfileApiService {
    @GET("v1/students/me")
    suspend fun getDetailedProfile(): BaseResponse<StudentResponse>

    @PATCH("v1/students/me")
    suspend fun updateProfile(@Body request: UpdateProfileRequest): BaseResponse<StudentResponse>

    @Multipart
    @POST("v1/uploads/avatar")
    suspend fun uploadAvatar(@Part file: MultipartBody.Part): BaseResponse<FileUploadResponse>
}
