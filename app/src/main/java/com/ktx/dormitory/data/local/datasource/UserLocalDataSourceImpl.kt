package com.ktx.dormitory.data.local.datasource

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.ktx.dormitory.data.local.dao.UserProfileDao
import com.ktx.dormitory.data.local.entity.UserProfileEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.userDataStore by preferencesDataStore(name = "smart_dorm_prefs")

@Singleton
class UserLocalDataSourceImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val userProfileDao: UserProfileDao
) : UserLocalDataSource {

    private val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")

    override suspend fun saveLoginStatus(isLoggedIn: Boolean) {
        context.userDataStore.edit { it[IS_LOGGED_IN] = isLoggedIn }
    }

    override fun isLoggedIn(): Flow<Boolean> = context.userDataStore.data.map {
        it[IS_LOGGED_IN] ?: false
    }

    override fun getProfile(): Flow<UserProfileEntity?> = userProfileDao.getProfile()

    override suspend fun saveProfile(profile: UserProfileEntity) {
        userProfileDao.insertProfile(profile)
    }

    override suspend fun clearAllData() {
        context.userDataStore.edit { it.clear() }
        userProfileDao.clearProfile()
    }
}
