package com.ilyanvk.todoapp.di.modules

import com.ilyanvk.todoapp.data.localdatasource.LocalDataSource
import com.ilyanvk.todoapp.data.localdatasource.LocalDataSourceImpl
import com.ilyanvk.todoapp.data.remotedatasource.RemoteDataSource
import com.ilyanvk.todoapp.data.remotedatasource.RemoteDataSourceImpl
import com.ilyanvk.todoapp.data.sharedpreferences.SharedPreferencesDataSource
import com.ilyanvk.todoapp.data.sharedpreferences.SharedPreferencesDataSourceImpl
import com.ilyanvk.todoapp.di.scopes.AppScope
import dagger.Binds
import dagger.Module

@Module
interface DataSourceModule {
    @AppScope
    @Binds
    fun bindRemoteDataSource(todoRemoteDataSourceImpl: RemoteDataSourceImpl): RemoteDataSource

    @AppScope
    @Binds
    fun bindLocalDataSource(localDataSourceImpl: LocalDataSourceImpl): LocalDataSource

    @AppScope
    @Binds
    fun bindSharedPreferencesDataSource(
        sharedPreferencesDataSource: SharedPreferencesDataSourceImpl
    ): SharedPreferencesDataSource
}
