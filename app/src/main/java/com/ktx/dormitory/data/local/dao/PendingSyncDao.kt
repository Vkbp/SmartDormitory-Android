package com.ktx.dormitory.data.local.dao

import androidx.room.*
import com.ktx.dormitory.data.local.entity.PendingSyncEntity
import com.ktx.dormitory.data.local.entity.SyncStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface PendingSyncDao {
    @Query("SELECT * FROM pending_sync WHERE syncStatus = :status ORDER BY createdAt ASC")
    fun getPendingActions(status: SyncStatus = SyncStatus.PENDING): Flow<List<PendingSyncEntity>>

    @Query("SELECT * FROM pending_sync WHERE syncStatus != :status")
    suspend fun getNotCompletedActions(status: SyncStatus = SyncStatus.COMPLETED): List<PendingSyncEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAction(action: PendingSyncEntity): Long

    @Update
    suspend fun updateAction(action: PendingSyncEntity)

    @Delete
    suspend fun deleteAction(action: PendingSyncEntity)

    @Query("DELETE FROM pending_sync WHERE syncStatus = :status")
    suspend fun deleteCompletedActions(status: SyncStatus = SyncStatus.COMPLETED)
}
