package com.itsjeel01.finsiblefrontend.data.di

import com.itsjeel01.finsiblefrontend.BuildConfig
import com.itsjeel01.finsiblefrontend.common.PreferenceManager
import com.itsjeel01.finsiblefrontend.common.Strings
import com.itsjeel01.finsiblefrontend.data.remote.api.AuthApiService
import com.itsjeel01.finsiblefrontend.data.remote.api.CategoryApiService
import com.itsjeel01.finsiblefrontend.data.remote.converter.ConverterFactory
import com.itsjeel01.finsiblefrontend.data.remote.converter.ResponseProcessor
import com.itsjeel01.finsiblefrontend.data.remote.interceptor.AuthInterceptor
import com.itsjeel01.finsiblefrontend.data.sync.CacheManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.OkHttpClient
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
    @Singleton
    fun okHttpClient(preferenceManager: PreferenceManager): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(preferenceManager))
            .build()
    }

    @Provides
    fun retrofit(okHttpClient: OkHttpClient, cacheManager: CacheManager): Retrofit {
        val contentType = MediaType.get(Strings.JSON_CONTENT_TYPE)
        val json = Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        }

        val jsonConverter = json.asConverterFactory(contentType)
        val processor = ResponseProcessor(cacheManager)
        val converter = ConverterFactory.create(jsonConverter, processor)

        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(converter)
            .client(okHttpClient)
            .build()
    }
}
