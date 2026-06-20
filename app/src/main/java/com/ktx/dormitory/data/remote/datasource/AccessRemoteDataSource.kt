package com.ktx.dormitory.data.remote.datasource

import com.ktx.dormitory.data.remote.dto.BaseResponse
import com.ktx.dormitory.data.remote.dto.common.AccessLogDto
import com.ktx.dormitory.data.remote.dto.common.FaceRegisterRequestDto

interface AccessRemoteDataSource {
    suspend fun verifyQrCode(qrCode: String): BaseResponse<Unit>
    suspend fun registerFaceOnServer(request: FaceRegisterRequestDto): BaseResponse<Unit>
    suspend fun getAccessHistory(): BaseResponse<List<AccessLogDto>>
}
