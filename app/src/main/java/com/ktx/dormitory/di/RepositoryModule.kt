package com.ktx.dormitory.di

import com.ktx.dormitory.data.auth.repository.AuthRepositoryImpl
import com.ktx.dormitory.data.profile.repository.ProfileRepositoryImpl
import com.ktx.dormitory.data.room.repository.RoomRepositoryImpl
import com.ktx.dormitory.data.application.repository.ApplicationRepositoryImpl
import com.ktx.dormitory.data.payment.repository.PaymentRepositoryImpl
import com.ktx.dormitory.data.access.repository.AccessRepositoryImpl
import com.ktx.dormitory.data.face.repository.FaceRepositoryImpl
import com.ktx.dormitory.data.settings.repository.SettingsRepositoryImpl
import com.ktx.dormitory.domain.auth.repository.AuthRepository
import com.ktx.dormitory.domain.profile.repository.ProfileRepository
import com.ktx.dormitory.domain.room.repository.RoomRepository
import com.ktx.dormitory.domain.application.repository.ApplicationRepository
import com.ktx.dormitory.domain.payment.repository.PaymentRepository
import com.ktx.dormitory.domain.access.repository.AccessRepository
import com.ktx.dormitory.domain.face.repository.FaceRepository
import com.ktx.dormitory.domain.settings.repository.SettingsRepository
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
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindProfileRepository(
        profileRepositoryImpl: ProfileRepositoryImpl
    ): ProfileRepository

    @Binds
    @Singleton
    abstract fun bindRoomRepository(
        roomRepositoryImpl: RoomRepositoryImpl
    ): RoomRepository

    @Binds
    @Singleton
    abstract fun bindApplicationRepository(
        applicationRepositoryImpl: ApplicationRepositoryImpl
    ): ApplicationRepository

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
