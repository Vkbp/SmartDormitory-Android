package com.ktx.dormitory.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "smart_dorm_prefs")

class AppDataStore(private val context: Context) {

    private val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
    private val STUDENT_NAME = stringPreferencesKey("student_name")
    private val ROOM_NUMBER = stringPreferencesKey("room_number")
    private val HAS_PAID = booleanPreferencesKey("has_paid_this_month")
    private val EXTEND_MONTHS = intPreferencesKey("extend_months")

    // Save login state
    suspend fun saveLoginState(isLoggedIn: Boolean, name: String = "", room: String = "") {
        context.dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN] = isLoggedIn
            preferences[STUDENT_NAME] = name
            preferences[ROOM_NUMBER] = room
        }
    }

    val isLoggedIn: Flow<Boolean> = context.dataStore.data.map { it[IS_LOGGED_IN] ?: false }

    // Payment state
    suspend fun savePaymentStatus(hasPaid: Boolean) {
        context.dataStore.edit { it[HAS_PAID] = hasPaid }
    }

    val hasPaidThisMonth: Flow<Boolean> = context.dataStore.data.map { it[HAS_PAID] ?: false }

    // Extend state
    suspend fun saveExtendMonths(months: Int) {
        context.dataStore.edit { it[EXTEND_MONTHS] = months }
    }

    val extendMonths: Flow<Int> = context.dataStore.data.map { it[EXTEND_MONTHS] ?: 0 }
}