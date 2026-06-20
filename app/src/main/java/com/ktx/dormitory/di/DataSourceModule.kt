package com.ktx.dormitory.di

import com.ktx.dormitory.data.local.datasource.*
import com.ktx.dormitory.data.remote.datasource.*
import com.ktx.dormitory.data.face.local.*
import com.ktx.dormitory.data.face.remote.*
import com.ktx.dormitory.data.local.TokenManager
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
    abstract fun bindUserLocalDataSource(
        userLocalDataSourceImpl: UserLocalDataSourceImpl
    ): UserLocalDataSource

    @Binds
    @Singleton
    abstract fun bindUserRemoteDataSource(
        userRemoteDataSourceImpl: UserRemoteDataSourceImpl
    ): UserRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindAuthRemoteDataSource(
        authRemoteDataSourceImpl: AuthRemoteDataSourceImpl
    ): AuthRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindAuthLocalDataSource(
        tokenManager: TokenManager
    ): AuthLocalDataSource

    @Binds
    @Singleton
    abstract fun bindPaymentRemoteDataSource(
        paymentRemoteDataSourceImpl: PaymentRemoteDataSourceImpl
    ): PaymentRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindAccessRemoteDataSource(
        accessRemoteDataSourceImpl: AccessRemoteDataSourceImpl
    ): AccessRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindFaceLocalDataSource(
        faceLocalDataSourceImpl: FaceLocalDataSourceImpl
    ): FaceLocalDataSource

    @Binds
    @Singleton
    abstract fun bindFaceRemoteDataSource(
        faceRemoteDataSourceImpl: FaceRemoteDataSourceImpl
    ): FaceRemoteDataSource
}


