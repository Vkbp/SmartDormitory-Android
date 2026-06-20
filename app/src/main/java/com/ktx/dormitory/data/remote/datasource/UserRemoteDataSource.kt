package com.ktx.dormitory.data.remote.datasource

import com.ktx.dormitory.data.remote.dto.BaseResponse
import com.ktx.dormitory.data.remote.dto.user.*
import okhttp3.MultipartBody

interface UserRemoteDataSource {
    suspend fun getDetailedProfile(): BaseResponse<StudentResponse>
    suspend fun updateProfile(request: UpdateProfileRequest): BaseResponse<StudentResponse>
    suspend fun uploadAvatar(file: MultipartBody.Part): BaseResponse<String>
    suspend fun getMyRoom(): BaseResponse<RoomInfoDto>
    suspend fun getMyApplication(): BaseResponse<DormApplicationDto>
    suspend fun getPaymentHistory(): BaseResponse<List<TransactionDto>>
    suspend fun getMyBills(): BaseResponse<List<InvoiceDto>>
}
