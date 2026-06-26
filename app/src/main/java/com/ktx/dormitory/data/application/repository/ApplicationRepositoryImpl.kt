package com.ktx.dormitory.data.application.repository

import com.ktx.dormitory.data.application.mapper.toDomain
import com.ktx.dormitory.data.application.remote.ApplicationRemoteDataSource
import com.ktx.dormitory.data.profile.local.ProfileLocalDataSource
import com.ktx.dormitory.domain.application.model.DormApplication
import com.ktx.dormitory.domain.application.repository.ApplicationRepository
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApplicationRepositoryImpl @Inject constructor(
    private val remoteDataSource: ApplicationRemoteDataSource,
    private val profileLocalDataSource: ProfileLocalDataSource
) : ApplicationRepository {

    override suspend fun getApplicationTimeline(): Result<DormApplication> {
        return try {
            val profile = profileLocalDataSource.getProfile().firstOrNull()
            val cccd = profile?.cccd ?: return Result.failure(Exception("Vui lòng tải hồ sơ trước khi xem đơn đăng ký"))

            val response = remoteDataSource.getMyApplication(cccd)
            if (response.success && response.data != null) {
                Result.success(response.data.toDomain())
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
