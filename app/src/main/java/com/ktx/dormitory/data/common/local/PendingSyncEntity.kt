package com.ktx.dormitory.data.common.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pending_sync")
data class PendingSyncEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val actionType: String,
    val payload: String,
    val syncStatus: SyncStatus = SyncStatus.PENDING,
    val retryCount: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)

enum class SyncStatus {
    PENDING, SYNCING, COMPLETED, FAILED
}
