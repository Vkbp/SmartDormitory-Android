package com.ktx.dormitory.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ktx.dormitory.data.local.entity.DormRequestEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DormRequestDao {
    @Query("SELECT * FROM dorm_requests ORDER BY createdAt DESC")
    fun getAllRequests(): Flow<List<DormRequestEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRequests(requests: List<DormRequestEntity>)

    @Query("DELETE FROM dorm_requests")
    suspend fun clearAll()
}
