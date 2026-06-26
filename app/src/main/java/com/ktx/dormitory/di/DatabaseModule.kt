package com.ktx.dormitory.di

import android.content.Context
import androidx.room.Room
import com.ktx.dormitory.data.local.AppDatabase
import com.ktx.dormitory.data.profile.local.UserProfileDao
import com.ktx.dormitory.data.payment.local.InvoiceDao
import com.ktx.dormitory.data.access.local.AccessLogDao
import com.ktx.dormitory.data.face.local.FaceDao
import com.ktx.dormitory.data.common.local.PendingSyncDao
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
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "smart_dorm_db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideAccessLogDao(db: AppDatabase): AccessLogDao = db.accessLogDao()

    @Provides
    fun provideFaceDao(db: AppDatabase): FaceDao = db.faceDao()

    @Provides
    fun provideUserProfileDao(db: AppDatabase): UserProfileDao = db.userProfileDao()

    @Provides
    fun provideInvoiceDao(db: AppDatabase): InvoiceDao = db.invoiceDao()

    @Provides
    fun providePendingSyncDao(db: AppDatabase): PendingSyncDao = db.pendingSyncDao()
}
