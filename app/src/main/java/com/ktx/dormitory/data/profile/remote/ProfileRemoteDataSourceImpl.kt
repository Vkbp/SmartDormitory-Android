package com.ktx.dormitory.data.profile.remote

import okhttp3.MultipartBody
import javax.inject.Inject
import javax.inject.Singleton
import com.ktx.dormitory.data.profile.dto.UpdateProfileRequest

@Singleton
class ProfileRemoteDataSourceImpl @Inject constructor(
    private val api: ProfileApiService
) : ProfileRemoteDataSource {
    override suspend fun getDetailedProfile() = api.getDetailedProfile()
    override suspend fun updateProfile(request: UpdateProfileRequest) = api.updateProfile(request)
    override suspend fun uploadAvatar(file: MultipartBody.Part) = api.uploadAvatar(file)
}
