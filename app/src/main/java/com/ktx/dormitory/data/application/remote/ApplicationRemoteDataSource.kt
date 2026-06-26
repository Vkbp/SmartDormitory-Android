package com.ktx.dormitory.data.application.remote

import com.ktx.dormitory.data.common.dto.BaseResponse
import com.ktx.dormitory.data.application.dto.DormApplicationDto

interface ApplicationRemoteDataSource {
    suspend fun getMyApplication(cccd: String): BaseResponse<DormApplicationDto>
}
