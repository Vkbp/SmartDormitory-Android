package com.ktx.dormitory.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

import com.ktx.dormitory.data.local.dao.*
import com.ktx.dormitory.data.local.entity.*
import com.ktx.dormitory.data.face.local.FaceDao
import com.ktx.dormitory.data.face.local.FaceEntity

@Database(
    entities = [
        AccessLogEntity::class, 
        FaceEntity::class,
        UserProfileEntity::class,
        NotificationEntity::class,
        DormRequestEntity::class,
        InvoiceEntity::class,
        PendingSyncEntity::class
    ], 
    version = 7, 
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun accessLogDao(): AccessLogDao
    abstract fun faceDao(): FaceDao
    abstract fun userProfileDao(): UserProfileDao
    abstract fun notificationDao(): NotificationDao
    abstract fun dormRequestDao(): DormRequestDao
    abstract fun invoiceDao(): InvoiceDao
    abstract fun pendingSyncDao(): PendingSyncDao
}
