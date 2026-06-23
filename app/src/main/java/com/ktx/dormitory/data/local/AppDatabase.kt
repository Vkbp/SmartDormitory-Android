package com.ktx.dormitory.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

import com.ktx.dormitory.data.local.dao.*
import com.ktx.dormitory.data.local.entity.*
import com.ktx.dormitory.data.face.local.FaceDao
import com.ktx.dormitory.data.face.local.FaceEntity

/**
 * AppDatabase - Room DB chỉ chứa các entity còn dùng.
 * NotificationEntity và DormRequestEntity đã bị loại bỏ vì Backend không có API tương ứng.
 * Tăng version lên 8 do thay đổi schema (xóa bảng notifications, dorm_requests).
 */
@Database(
    entities = [
        AccessLogEntity::class,
        FaceEntity::class,
        UserProfileEntity::class,
        InvoiceEntity::class,
        PendingSyncEntity::class
    ],
    version = 8,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun accessLogDao(): AccessLogDao
    abstract fun faceDao(): FaceDao
    abstract fun userProfileDao(): UserProfileDao
    abstract fun invoiceDao(): InvoiceDao
    abstract fun pendingSyncDao(): PendingSyncDao
}
