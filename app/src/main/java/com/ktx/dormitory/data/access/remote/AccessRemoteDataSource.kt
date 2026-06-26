package com.ktx.dormitory.data.access.remote

import com.ktx.dormitory.data.access.dto.AccessLogDto
import com.ktx.dormitory.data.common.dto.PageResponse

interface AccessRemoteDataSource {
    suspend fun getAccessHistory(studentId: String): PageResponse<AccessLogDto>
}
