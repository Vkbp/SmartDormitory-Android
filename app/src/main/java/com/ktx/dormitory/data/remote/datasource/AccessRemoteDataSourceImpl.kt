package com.ktx.dormitory.data.remote.datasource

import com.ktx.dormitory.data.remote.api.AccessApiService
import com.ktx.dormitory.data.remote.dto.BaseResponse
import com.ktx.dormitory.data.remote.dto.common.AccessLogDto
import com.ktx.dormitory.data.remote.dto.common.FaceRegisterRequestDto
import javax.inject.Inject

class AccessRemoteDataSourceImpl @Inject constructor(
    private val api: AccessApiService
) : AccessRemoteDataSource {
    override suspend fun verifyQrCode(qrCode: String): BaseResponse<Unit> = api.verifyQrCode(qrCode)
    override suspend fun registerFaceOnServer(request: FaceRegisterRequestDto): BaseResponse<Unit> = 
        api.registerFaceOnServer(request)
    override suspend fun getAccessHistory(): BaseResponse<List<AccessLogDto>> = api.getAccessHistory()
}
