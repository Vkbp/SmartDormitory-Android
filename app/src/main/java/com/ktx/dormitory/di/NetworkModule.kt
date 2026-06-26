package com.ktx.dormitory.di

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.ktx.dormitory.core.Constants
import com.ktx.dormitory.core.network.AuthInterceptor
import com.ktx.dormitory.core.network.IdempotencyInterceptor
import com.ktx.dormitory.core.network.RetryInterceptor
import com.ktx.dormitory.core.network.TokenAuthenticator
import com.ktx.dormitory.core.network.NetworkMonitor
import com.ktx.dormitory.data.auth.remote.AuthApiService
import com.ktx.dormitory.data.profile.remote.ProfileApiService
import com.ktx.dormitory.data.room.remote.RoomApiService
import com.ktx.dormitory.data.application.remote.ApplicationApiService
import com.ktx.dormitory.data.payment.remote.PaymentApiService
import com.ktx.dormitory.data.access.remote.AccessApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        idempotencyInterceptor: IdempotencyInterceptor,
        tokenAuthenticator: TokenAuthenticator
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(idempotencyInterceptor)
            .addInterceptor(RetryInterceptor(maxRetry = 2))
            .addInterceptor(authInterceptor)
            .authenticator(tokenAuthenticator)
            .connectTimeout(Constants.NETWORK_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(Constants.NETWORK_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(Constants.NETWORK_TIMEOUT, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideIdempotencyInterceptor(): IdempotencyInterceptor {
        return IdempotencyInterceptor()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApiService {
        return retrofit.create(AuthApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideProfileApi(retrofit: Retrofit): ProfileApiService {
        return retrofit.create(ProfileApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideRoomApi(retrofit: Retrofit): RoomApiService {
        return retrofit.create(RoomApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideApplicationApi(retrofit: Retrofit): ApplicationApiService {
        return retrofit.create(ApplicationApiService::class.java)
    }

    @Provides
    @Singleton
    fun providePaymentApi(retrofit: Retrofit): PaymentApiService {
        return retrofit.create(PaymentApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideAccessApi(retrofit: Retrofit): AccessApiService {
        return retrofit.create(AccessApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideNetworkMonitor(@ApplicationContext context: Context): NetworkMonitor {
        return NetworkMonitor(context)
    }

    @Provides
    @Singleton
    fun provideEncryptedSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        return EncryptedSharedPreferences.create(
            context,
            "secure_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }
}
