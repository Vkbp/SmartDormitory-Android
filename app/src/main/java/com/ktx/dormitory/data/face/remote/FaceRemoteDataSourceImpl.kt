package com.ktx.dormitory.data.face.remote

import com.ktx.dormitory.data.remote.api.AccessApiService
import com.ktx.dormitory.data.remote.dto.BaseResponse
import com.ktx.dormitory.data.remote.dto.common.FaceRegisterRequestDto
import javax.inject.Inject

class FaceRemoteDataSourceImpl @Inject constructor(
    private val api: AccessApiService
) : FaceRemoteDataSource {
    override suspend fun registerFaceOnServer(request: FaceRegisterRequestDto): BaseResponse<Unit> = 
        api.registerFaceOnServer(request)
}
