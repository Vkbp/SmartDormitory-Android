package com.ktx.dormitory.data.remote.datasource

import com.ktx.dormitory.data.remote.dto.BaseResponse
import com.ktx.dormitory.data.remote.dto.common.AccessLogDto

interface AccessRemoteDataSource {
    suspend fun registerFaceOnServer(studentId: String, faceImageUrl: String): String
    suspend fun getAccessHistory(studentId: String): com.ktx.dormitory.data.remote.dto.common.PageResponse<AccessLogDto>
}
