package com.ilyanvk.todoapp.di.modules

import com.ilyanvk.todoapp.data.localdatasource.LocalDataSource
import com.ilyanvk.todoapp.data.localdatasource.LocalDataSourceImpl
import com.ilyanvk.todoapp.data.remotedatasource.RemoteDataSource
import com.ilyanvk.todoapp.data.remotedatasource.RemoteDataSourceImpl
import com.ilyanvk.todoapp.data.sharedpreferences.SharedPreferencesDataSource
import com.ilyanvk.todoapp.data.sharedpreferences.SharedPreferencesDataSourceImpl
import dagger.Binds
import dagger.Module

@Module
interface DataSourceModule {
    @Binds
    fun bindRemoteDataSource(todoRemoteDataSourceImpl: RemoteDataSourceImpl): RemoteDataSource

    @Binds
    fun bindLocalDataSource(localDataSourceImpl: LocalDataSourceImpl): LocalDataSource

    @Binds
    fun bindSharedPreferencesDataSource(
        sharedPreferencesDataSource: SharedPreferencesDataSourceImpl
    ): SharedPreferencesDataSource
}
