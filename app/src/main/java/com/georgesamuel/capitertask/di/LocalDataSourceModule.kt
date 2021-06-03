package com.georgesamuel.capitertask.di

import com.georgesamuel.capitertask.data.LocalDataSource
import com.georgesamuel.capitertask.data.LocalDataSourceImpl
import com.georgesamuel.capitertask.db.AppDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class LocalDataSourceModule {

    @Singleton
    @Provides
    fun provideLocalDataSource(appDao: AppDao): LocalDataSource {
        return LocalDataSourceImpl(appDao)
    }

}