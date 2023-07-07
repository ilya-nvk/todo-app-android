package com.ilyanvk.todoapp.di.modules

import com.ilyanvk.todoapp.di.scopes.AppScope
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

@Module
class ClientModule {
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val authorizationInterceptor = Interceptor { chain ->
        val request =
            chain.request().newBuilder().addHeader("Authorization", "Bearer $AUTH_TOKEN").build()
        chain.proceed(request)
    }

    @AppScope
    @Provides
    fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder().connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS).addInterceptor(loggingInterceptor)
            .addInterceptor(authorizationInterceptor).build()

    companion object Constants {
        const val AUTH_TOKEN = "outsparspinning"
        const val CONNECT_TIMEOUT = 120L
        const val READ_TIMEOUT = 120L
        const val WRITE_TIMEOUT = 90L
    }
}
