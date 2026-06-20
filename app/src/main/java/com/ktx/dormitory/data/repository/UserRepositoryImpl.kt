package com.ktx.dormitory.data.repository

import com.google.gson.Gson
import com.ktx.dormitory.core.network.NetworkMonitor
import com.ktx.dormitory.core.sync.SyncScheduler
import com.ktx.dormitory.data.local.dao.PendingSyncDao
import com.ktx.dormitory.data.local.datasource.AuthLocalDataSource
import com.ktx.dormitory.data.local.datasource.UserLocalDataSource
import com.ktx.dormitory.data.local.entity.PendingSyncEntity
import com.ktx.dormitory.data.remote.datasource.UserRemoteDataSource
import com.ktx.dormitory.data.mapper.*
import com.ktx.dormitory.domain.model.*
import com.ktx.dormitory.domain.repository.UserRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val remoteDataSource: UserRemoteDataSource,
    private val localDataSource: UserLocalDataSource,
    private val authLocalDataSource: AuthLocalDataSource,
    private val pendingSyncDao: PendingSyncDao,
    private val syncScheduler: SyncScheduler,
    private val networkMonitor: NetworkMonitor
) : UserRepository {

    private val updateMutex = Mutex()
    private val gson = Gson()

    override suspend fun getProfile(): Result<UserProfile> {
        return try {
            val response = remoteDataSource.getDetailedProfile()
            if (response.success && response.data != null) {
                val profile = response.data.toDomain()
                localDataSource.saveProfile(response.data.toEntity())
                Result.success(profile)
            } else {
                val cached = localDataSource.getProfile().firstOrNull()?.toDomain()
                if (cached != null) Result.success(cached)
                else Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            val cached = localDataSource.getProfile().firstOrNull()?.toDomain()
            if (cached != null) Result.success(cached)
            else Result.failure(e)
        }
    }

    override suspend fun updateProfile(request: UpdateProfileRequest): Result<Unit> {
        return updateMutex.withLock {
            val payload = gson.toJson(request)
            try {
                val isOnline = networkMonitor.isOnline.first()
                if (!isOnline) {
                    return@withLock queueAction("UPDATE_PROFILE", payload)
                }
                val response = remoteDataSource.updateProfile(request.toDto())
                if (response.success) Result.success(Unit) else Result.failure(Exception(response.message))
            } catch (e: Exception) {
                if (isNetworkException(e)) {
                    queueAction("UPDATE_PROFILE", payload)
                } else {
                    Result.failure(e)
                }
            }
        }
    }

    private suspend fun queueAction(actionType: String, payload: String): Result<Unit> {
        pendingSyncDao.insertAction(
            PendingSyncEntity(
                actionType = actionType,
                payload = payload
            )
        )
        syncScheduler.scheduleSync()
        return Result.success(Unit)
    }

    private fun isNetworkException(e: Exception): Boolean {
        return e is java.io.IOException
    }

    override suspend fun getRoomInfo(): Result<RoomInfo> {
        return try {
            val response = remoteDataSource.getMyRoom()
            if (response.success && response.data != null) {
                Result.success(response.data.toDomain())
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getApplicationTimeline(): Result<DormApplication> {
        return try {
            val response = remoteDataSource.getMyApplication()
            if (response.success && response.data != null) {
                Result.success(response.data.toDomain())
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getPaymentHistory(): Result<List<Transaction>> {
        return try {
            val response = remoteDataSource.getPaymentHistory()
            if (response.success && response.data != null) {
                Result.success(response.data.map { it.toDomain() })
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMyBills(): Result<List<Invoice>> {
        return try {
            val response = remoteDataSource.getMyBills()
            if (response.success && response.data != null) {
                Result.success(response.data.map { it.toDomain() })
            } else {
                Result.failure(Exception("Không có dữ liệu hóa đơn"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun uploadAvatar(filePath: String): Result<String> {
        return try {
            val file = File(filePath)
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
            
            val response = remoteDataSource.uploadAvatar(body)
            if (response.success && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun saveLoginStatus(isLoggedIn: Boolean) {
        localDataSource.saveLoginStatus(isLoggedIn)
    }

    override suspend fun clearAllData() {
        authLocalDataSource.clearTokens()
        localDataSource.clearAllData()
    }
}
