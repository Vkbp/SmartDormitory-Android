package com.ktx.dormitory.data.remote.datasource

import com.ktx.dormitory.data.remote.api.AccessApiService
import com.ktx.dormitory.data.remote.dto.common.AccessLogDto
import com.ktx.dormitory.data.remote.dto.common.PageResponse
import javax.inject.Inject

class AccessRemoteDataSourceImpl @Inject constructor(
    private val api: AccessApiService
) : AccessRemoteDataSource {

    override suspend fun registerFaceOnServer(studentId: String, faceImageUrl: String): String =
        api.registerFaceOnServer(studentId, hashMapOf("faceImageUrl" to faceImageUrl))

    override suspend fun getAccessHistory(studentId: String): PageResponse<AccessLogDto> =
        api.getAccessHistory(studentId)
}
