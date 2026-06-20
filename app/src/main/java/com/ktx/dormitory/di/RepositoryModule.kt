package com.ktx.dormitory.di

import com.ktx.dormitory.data.repository.*
import com.ktx.dormitory.data.face.repository.FaceRepositoryImpl
import com.ktx.dormitory.domain.repository.*
import com.ktx.dormitory.domain.face.repository.FaceRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindNotificationRepository(
        notificationRepositoryImpl: NotificationRepositoryImpl
    ): NotificationRepository

    @Binds
    @Singleton
    abstract fun bindRequestRepository(
        requestRepositoryImpl: RequestRepositoryImpl
    ): RequestRepository

    @Binds
    @Singleton
    abstract fun bindPaymentRepository(
        paymentRepositoryImpl: PaymentRepositoryImpl
    ): PaymentRepository

    @Binds
    @Singleton
    abstract fun bindAccessRepository(
        accessRepositoryImpl: AccessRepositoryImpl
    ): AccessRepository

    @Binds
    @Singleton
    abstract fun bindFaceRepository(
        faceRepositoryImpl: FaceRepositoryImpl
    ): FaceRepository

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(
        settingsRepositoryImpl: SettingsRepositoryImpl
    ): SettingsRepository
}

