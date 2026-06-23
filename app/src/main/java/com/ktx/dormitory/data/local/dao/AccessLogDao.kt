package com.ktx.dormitory.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ktx.dormitory.data.local.AccessLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AccessLogDao {
    @Query("SELECT * FROM access_logs ORDER BY eventTimestamp DESC")
    fun getAllLogs(): Flow<List<AccessLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLogs(logs: List<AccessLogEntity>)

    @Query("DELETE FROM access_logs")
    suspend fun clearAll()
}
