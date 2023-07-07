package com.ilyanvk.todoapp.di.modules

import android.content.Context
import android.content.SharedPreferences
import com.ilyanvk.todoapp.data.sharedpreferences.SharedPreferencesDataSource.Companion.NAME
import com.ilyanvk.todoapp.di.scopes.AppScope
import dagger.Module
import dagger.Provides

@Module
class AppModule(private val context: Context) {
    @AppScope
    @Provides
    fun provideContext(): Context {
        return context
    }

    @AppScope
    @Provides
    fun provideSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(NAME, Context.MODE_PRIVATE)
    }
}
