package com.ktx.dormitory.di

import com.ktx.dormitory.data.repository.AccessRepositoryImpl
import com.ktx.dormitory.data.repository.AuthRepositoryImpl
import com.ktx.dormitory.data.repository.NotificationRepositoryImpl
import com.ktx.dormitory.data.repository.PaymentRepositoryImpl
import com.ktx.dormitory.data.repository.RequestRepositoryImpl
import com.ktx.dormitory.data.repository.UserRepositoryImpl
import com.ktx.dormitory.domain.repository.AccessRepository
import com.ktx.dormitory.domain.repository.AuthRepository
import com.ktx.dormitory.domain.repository.NotificationRepository
import com.ktx.dormitory.domain.repository.PaymentRepository
import com.ktx.dormitory.domain.repository.RequestRepository
import com.ktx.dormitory.domain.repository.UserRepository
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
}
