package com.georgesamuel.capitertask.di

import com.georgesamuel.capitertask.data.AppRepository
import com.georgesamuel.capitertask.data.LocalDataSource
import com.georgesamuel.capitertask.db.AppDao
import com.georgesamuel.capitertask.usecases.*
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class UseCaseModule {

    @Singleton
    @Provides
    fun providesGetProductsUseCase(appRepository: AppRepository): GetProductsUseCase{
        return GetProductsUseCase(appRepository)
    }

    @Singleton
    @Provides
    fun providesCreateOrderUseCase(appRepository: AppRepository): CreateOrderUseCase{
        return CreateOrderUseCase(appRepository)
    }

    @Singleton
    @Provides
    fun providesAddToCartUseCase(localDataSource: LocalDataSource): AddToCartUseCase{
        return AddToCartUseCase(localDataSource)
    }

    @Singleton
    @Provides
    fun providesGetCartCartUseCase(localDataSource: LocalDataSource): GetCartUseCase{
        return GetCartUseCase(localDataSource)
    }

    @Singleton
    @Provides
    fun providesRemoveProductCartUseCaseUseCase(localDataSource: LocalDataSource): RemoveProductCartUseCase{
        return RemoveProductCartUseCase(localDataSource)
    }

    @Singleton
    @Provides
    fun providesDeleteCartUseCaseUseCase(localDataSource: LocalDataSource): DeleteCartUseCase{
        return DeleteCartUseCase(localDataSource)
    }
}