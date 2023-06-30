package com.ilyanvk.todoapp.data.retrofit

import com.ilyanvk.todoapp.data.Constants.AUTH_TOKEN
import com.ilyanvk.todoapp.data.Constants.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiClient {
    private val timeoutDuration = 500L

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(timeoutDuration, TimeUnit.SECONDS)
        .readTimeout(timeoutDuration, TimeUnit.SECONDS)
        .writeTimeout(timeoutDuration, TimeUnit.SECONDS)
        .addInterceptor(loggingInterceptor)
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $AUTH_TOKEN")
                .build()
            chain.proceed(request)
        }
        .build()

    private val retrofit = Retrofit.Builder().client(httpClient).baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create()).build()

    val api: TodoItemApi by lazy {
        retrofit.create(TodoItemApi::class.java)
    }
}