package com.itsjeel01.finsiblefrontend.data.di

import com.itsjeel01.finsiblefrontend.BuildConfig
import com.itsjeel01.finsiblefrontend.common.PreferenceManager
import com.itsjeel01.finsiblefrontend.data.remote.api.AccountApiService
import com.itsjeel01.finsiblefrontend.data.remote.api.AccountGroupApiService
import com.itsjeel01.finsiblefrontend.data.remote.api.AuthApiService
import com.itsjeel01.finsiblefrontend.data.remote.api.CategoryApiService
import com.itsjeel01.finsiblefrontend.data.remote.api.SyncApiService
import com.itsjeel01.finsiblefrontend.data.remote.api.TransactionApiService
import com.itsjeel01.finsiblefrontend.data.remote.converter.ResponseHandler
import com.itsjeel01.finsiblefrontend.data.remote.converter.ResponseHandlingConverterFactory
import com.itsjeel01.finsiblefrontend.data.remote.interceptor.AuthInterceptor
import com.itsjeel01.finsiblefrontend.data.remote.interceptor.MockInterceptor
import com.itsjeel01.finsiblefrontend.data.sync.CacheManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun categoryApiService(retrofit: Retrofit): CategoryApiService =
        retrofit.create(CategoryApiService::class.java)

    @Provides
    fun authApiService(retrofit: Retrofit): AuthApiService =
        retrofit.create(AuthApiService::class.java)

    @Provides
    fun accountGroupApiService(retrofit: Retrofit): AccountGroupApiService =
        retrofit.create(AccountGroupApiService::class.java)

    @Provides
    fun accountApiService(retrofit: Retrofit): AccountApiService =
        retrofit.create(AccountApiService::class.java)

    @Provides
    fun transactionApiService(retrofit: Retrofit): TransactionApiService =
        retrofit.create(TransactionApiService::class.java)

    @Provides
    fun provideSyncApiService(retrofit: Retrofit): SyncApiService {
        return retrofit.create(SyncApiService::class.java)
    }

    @Provides
    @Singleton
    fun okHttpClient(
        preferenceManager: PreferenceManager,
        mockInterceptor: MockInterceptor?
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .apply {
                // Add MockInterceptor first (if available in debug build)
                if (BuildConfig.DEBUG && mockInterceptor != null) {
                    addInterceptor(mockInterceptor)
                }

                // Add HTTP logging interceptor for debug builds
                if (BuildConfig.DEBUG) {
                    val loggingInterceptor = HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    }
                    addInterceptor(loggingInterceptor)
                }
            }
            .addInterceptor(AuthInterceptor(preferenceManager))
            .build()
    }

    @Provides
    fun retrofit(okHttpClient: OkHttpClient, cacheManager: CacheManager): Retrofit {
        val contentType = "application/json".toMediaType()
        val json = Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        }

        val jsonConverter = json.asConverterFactory(contentType)
        val processor = ResponseHandler(cacheManager)
        val converter = ResponseHandlingConverterFactory.create(jsonConverter, processor)

        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(converter)
            .client(okHttpClient)
            .build()
    }
}

