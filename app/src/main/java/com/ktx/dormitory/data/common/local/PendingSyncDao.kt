package com.ktx.dormitory.data.common.local

import androidx.room.*

@Dao
interface PendingSyncDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAction(action: PendingSyncEntity)

    @Update
    suspend fun updateAction(action: PendingSyncEntity)

    @Delete
    suspend fun deleteAction(action: PendingSyncEntity)

    @Query("SELECT * FROM pending_sync WHERE syncStatus != 'COMPLETED' AND retryCount < 5")
    suspend fun getNotCompletedActions(): List<PendingSyncEntity>
}
