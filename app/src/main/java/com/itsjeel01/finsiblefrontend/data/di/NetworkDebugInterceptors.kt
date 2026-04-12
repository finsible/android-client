package com.itsjeel01.finsiblefrontend.data.di

import okhttp3.Interceptor

interface NetworkInterceptorsProvider {
	fun interceptors(): List<Interceptor>
}

