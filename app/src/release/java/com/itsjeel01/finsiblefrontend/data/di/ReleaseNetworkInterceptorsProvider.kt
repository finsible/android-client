package com.itsjeel01.finsiblefrontend.data.di

import okhttp3.Interceptor
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReleaseNetworkInterceptorsProvider @Inject constructor() : NetworkInterceptorsProvider {
    override fun interceptors(): List<Interceptor> = emptyList()
}

