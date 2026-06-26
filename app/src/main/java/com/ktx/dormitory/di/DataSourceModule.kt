package com.ktx.dormitory.di

import com.ktx.dormitory.data.auth.local.*
import com.ktx.dormitory.data.auth.remote.*
import com.ktx.dormitory.data.profile.local.*
import com.ktx.dormitory.data.profile.remote.*
import com.ktx.dormitory.data.room.remote.*
import com.ktx.dormitory.data.application.remote.*
import com.ktx.dormitory.data.payment.remote.*
import com.ktx.dormitory.data.access.remote.*
import com.ktx.dormitory.data.face.local.*
import com.ktx.dormitory.data.face.remote.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    @Singleton
    abstract fun bindAuthRemoteDataSource(
        impl: AuthRemoteDataSourceImpl
    ): AuthRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindAuthLocalDataSource(
        impl: TokenManager
    ): AuthLocalDataSource

    @Binds
    @Singleton
    abstract fun bindProfileRemoteDataSource(
        impl: ProfileRemoteDataSourceImpl
    ): ProfileRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindProfileLocalDataSource(
        impl: ProfileLocalDataSourceImpl
    ): ProfileLocalDataSource

    @Binds
    @Singleton
    abstract fun bindRoomRemoteDataSource(
        impl: RoomRemoteDataSourceImpl
    ): RoomRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindApplicationRemoteDataSource(
        impl: ApplicationRemoteDataSourceImpl
    ): ApplicationRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindPaymentRemoteDataSource(
        impl: PaymentRemoteDataSourceImpl
    ): PaymentRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindAccessRemoteDataSource(
        impl: AccessRemoteDataSourceImpl
    ): AccessRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindFaceLocalDataSource(
        impl: FaceLocalDataSourceImpl
    ): FaceLocalDataSource

    @Binds
    @Singleton
    abstract fun bindFaceRemoteDataSource(
        impl: FaceRemoteDataSourceImpl
    ): FaceRemoteDataSource
}
