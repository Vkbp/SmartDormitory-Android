package com.ktx.dormitory.data.remote.datasource

import com.ktx.dormitory.data.remote.api.UserApiService
import com.ktx.dormitory.data.remote.dto.BaseResponse
import com.ktx.dormitory.data.remote.dto.user.*
import okhttp3.MultipartBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRemoteDataSourceImpl @Inject constructor(
    private val api: UserApiService
) : UserRemoteDataSource {
    override suspend fun getDetailedProfile() = api.getDetailedProfile()
    override suspend fun updateProfile(request: UpdateProfileRequest) = api.updateProfile(request)
    override suspend fun uploadAvatar(file: MultipartBody.Part) = api.uploadAvatar(file)
    override suspend fun getMyRoom() = api.getMyRoom()
    override suspend fun getMyApplication() = api.getMyApplication()
    override suspend fun getPaymentHistory() = api.getPaymentHistory()
    override suspend fun getMyBills() = api.getMyBills()
}
