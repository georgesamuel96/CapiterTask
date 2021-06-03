package com.georgesamuel.capitertask.di

import com.georgesamuel.capitertask.data.APIService
import com.georgesamuel.capitertask.data.AppDataSource
import com.georgesamuel.capitertask.data.AppRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppRepositoryModule {

    @Provides
    @Singleton
    fun provideAppRepository(
        apiService: APIService
    ): AppRepository {
        return AppDataSource(
            apiService
        )
    }

}