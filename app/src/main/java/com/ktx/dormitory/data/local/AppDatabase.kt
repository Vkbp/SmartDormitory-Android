package com.ktx.dormitory.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [AccessLogEntity::class, FaceEntity::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun accessLogDao(): AccessLogDao
    abstract fun faceDao(): FaceDao
}
