package com.ilyanvk.todoapp.di.modules

import com.ilyanvk.todoapp.data.remotedatasource.retrofit.TodoItemApi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class ApiModule {

    @Singleton
    @Provides
    fun provideRetrofit(httpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder().client(httpClient).baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    @Provides
    fun provideApi(retrofitClient: Retrofit): TodoItemApi {
        return retrofitClient.create(TodoItemApi::class.java)
    }

    companion object {
        const val BASE_URL = "https://beta.mrdekk.ru/todobackend/"
    }
}
