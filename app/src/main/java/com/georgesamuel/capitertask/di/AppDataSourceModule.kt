package com.georgesamuel.capitertask.di

import com.georgesamuel.capitertask.data.APIService
import com.georgesamuel.capitertask.data.AppDataSource
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppDataSourceModule {

    @Singleton
    @Provides
    fun providesAppDataSource(apiService: APIService): AppDataSource {
        return AppDataSource(apiService)
    }
}