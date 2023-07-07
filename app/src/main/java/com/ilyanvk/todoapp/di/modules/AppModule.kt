package com.ilyanvk.todoapp.di.modules

import android.content.Context
import android.content.SharedPreferences
import com.ilyanvk.todoapp.data.sharedpreferences.SharedPreferencesDataSource.Companion.NAME
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val context: Context) {
    @Singleton
    @Provides
    fun provideContext(): Context {
        return context
    }

    @Singleton
    @Provides
    fun provideSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(NAME, Context.MODE_PRIVATE)
    }
}
