package com.ktx.dormitory.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.ktx.dormitory.data.local.AccessLogDao
import com.ktx.dormitory.data.local.AppDatabase
import com.ktx.dormitory.data.local.dao.*
import com.ktx.dormitory.data.face.local.FaceDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        val migration6To7 = object : Migration(6, 7) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS `pending_sync` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 
                        `actionType` TEXT NOT NULL, 
                        `payload` TEXT NOT NULL, 
                        `createdAt` INTEGER NOT NULL, 
                        `retryCount` INTEGER NOT NULL, 
                        `syncStatus` TEXT NOT NULL
                    )
                """.trimIndent())
                db.execSQL("CREATE INDEX IF NOT EXISTS index_pending_sync_syncStatus ON pending_sync (syncStatus)")
            }
        }

        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "smart_dorm_db"
        )
        .addMigrations(migration6To7)
        .build()
    }

    @Provides
    fun provideAccessLogDao(database: AppDatabase): AccessLogDao {
        return database.accessLogDao()
    }

    @Provides
    fun provideFaceDao(database: AppDatabase): FaceDao {
        return database.faceDao()
    }

    @Provides
    fun provideUserProfileDao(database: AppDatabase): UserProfileDao {
        return database.userProfileDao()
    }

    @Provides
    fun provideNotificationDao(database: AppDatabase): NotificationDao {
        return database.notificationDao()
    }

    @Provides
    fun provideDormRequestDao(database: AppDatabase): DormRequestDao {
        return database.dormRequestDao()
    }

    @Provides
    fun provideInvoiceDao(database: AppDatabase): InvoiceDao {
        return database.invoiceDao()
    }

    @Provides
    fun providePendingSyncDao(database: AppDatabase): PendingSyncDao {
        return database.pendingSyncDao()
    }
}
