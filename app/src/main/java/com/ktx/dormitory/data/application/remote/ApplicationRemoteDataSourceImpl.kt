package com.ktx.dormitory.data.application.remote

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApplicationRemoteDataSourceImpl @Inject constructor(
    private val api: ApplicationApiService
) : ApplicationRemoteDataSource {
    override suspend fun getMyApplication(cccd: String) = api.getMyApplication(cccd)
}
