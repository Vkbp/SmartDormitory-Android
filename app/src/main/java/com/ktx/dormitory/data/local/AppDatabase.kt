package com.ktx.dormitory.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

import com.ktx.dormitory.data.profile.local.UserProfileDao
import com.ktx.dormitory.data.profile.local.UserProfileEntity
import com.ktx.dormitory.data.payment.local.InvoiceDao
import com.ktx.dormitory.data.payment.local.InvoiceEntity
import com.ktx.dormitory.data.access.local.AccessLogDao
import com.ktx.dormitory.data.access.local.AccessLogEntity
import com.ktx.dormitory.data.face.local.FaceDao
import com.ktx.dormitory.data.face.local.FaceEntity
import com.ktx.dormitory.data.common.local.PendingSyncDao
import com.ktx.dormitory.data.common.local.PendingSyncEntity

/**
 * AppDatabase - Room DB.
 * Version 8.
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
