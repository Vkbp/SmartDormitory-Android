package com.ktx.dormitory.data.repository

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.ktx.dormitory.data.api.UserApiService
import com.ktx.dormitory.data.local.TokenManager
import com.ktx.dormitory.domain.model.*
import com.ktx.dormitory.domain.repository.UserRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

// Sử dụng chung tên dataStore để đồng nhất, hoặc inject DataStore trực tiếp
private val Context.userDataStore by preferencesDataStore(name = "smart_dorm_prefs")

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val api: UserApiService,
    private val tokenManager: TokenManager,
    @ApplicationContext private val context: Context
) : UserRepository {

    private val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")

    override suspend fun getProfile(): Result<UserProfile> {
        return try {
            val response = api.getDetailedProfile()
            if (response.success && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateProfile(request: UpdateProfileRequest): Result<Unit> {
        return try {
            val response = api.updateProfile(request)
            if (response.success) Result.success(Unit) else Result.failure(Exception(response.message))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getRoomInfo(): Result<RoomInfo> {
        return try {
            val response = api.getMyRoom()
            if (response.success && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getApplicationTimeline(): Result<DormApplication> {
        return try {
            val response = api.getMyApplication()
            if (response.success && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getPaymentHistory(): Result<List<Transaction>> {
        return try {
            val response = api.getPaymentHistory()
            if (response.success && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMyBills(): Result<List<Invoice>> {
        return try {
            val response = api.getMyBills()
            if (response.success && response.data != null) {
                Result.success(response.data)
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
            
            val response = api.uploadAvatar(body)
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
        context.userDataStore.edit { it[IS_LOGGED_IN] = isLoggedIn }
    }

    override suspend fun clearAllData() {
        tokenManager.clearTokens()
        context.userDataStore.edit { it.clear() }
    }
}
