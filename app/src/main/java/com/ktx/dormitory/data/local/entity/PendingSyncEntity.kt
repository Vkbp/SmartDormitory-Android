package com.ktx.dormitory.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "pending_sync",
    indices = [Index(value = ["syncStatus"])]
)
data class PendingSyncEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val actionType: String, // e.g., "CREATE_REQUEST", "PAY_INVOICE", "UPDATE_PROFILE"
    val payload: String,    // JSON payload
    val createdAt: Long = System.currentTimeMillis(),
    val retryCount: Int = 0,
    val syncStatus: SyncStatus = SyncStatus.PENDING
)

enum class SyncStatus {
    PENDING,
    SYNCING,
    FAILED,
    COMPLETED
}
