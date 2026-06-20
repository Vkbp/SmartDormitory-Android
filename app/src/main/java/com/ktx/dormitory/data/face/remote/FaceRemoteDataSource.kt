package com.ktx.dormitory.data.face.remote

import com.ktx.dormitory.data.remote.dto.BaseResponse
import com.ktx.dormitory.data.remote.dto.common.FaceRegisterRequestDto

interface FaceRemoteDataSource {
    suspend fun registerFaceOnServer(request: FaceRegisterRequestDto): BaseResponse<Unit>
}
