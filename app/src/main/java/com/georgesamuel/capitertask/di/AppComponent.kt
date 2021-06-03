package com.georgesamuel.capitertask.di

import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AppRepositoryModule::class,
    NetworkModule::class,
    AppDataSourceModule::class,
    UseCaseModule::class,
    DataBaseModule::class,
    LocalDataSourceModule::class,
    AppModule::class
])
interface AppComponent {

    fun appSubComponent():AppSubComponent.Factory

}