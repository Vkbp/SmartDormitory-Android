package com.ktx.dormitory.data.access.remote

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccessRemoteDataSourceImpl @Inject constructor(
    private val api: AccessApiService
) : AccessRemoteDataSource {
    override suspend fun getAccessHistory(studentId: String) = api.getAccessHistory(studentId)
}
