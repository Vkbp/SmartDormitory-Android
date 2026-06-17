package com.ktx.dormitory.di

import android.content.Context
import androidx.room.Room
import com.ktx.dormitory.data.local.AccessLogDao
import com.ktx.dormitory.data.local.AppDatabase
import com.ktx.dormitory.data.local.FaceDao
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
        )
        .fallbackToDestructiveMigration() // Thêm cái này để tự động cập nhật Database khi đổi Version
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
}
