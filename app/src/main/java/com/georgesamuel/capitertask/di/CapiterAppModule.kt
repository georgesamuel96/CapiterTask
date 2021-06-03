package com.georgesamuel.capitertask.di

import com.georgesamuel.capitertask.usecases.*
import com.georgesamuel.capitertask.viewmodel.AppViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class CapiterAppModule {
    @AppScope
    @Provides
    fun provideAppViewModelFactory(
        getProductsUseCase: GetProductsUseCase,
        addToCartUseCase: AddToCartUseCase,
        getCartUseCase: GetCartUseCase,
        removeProductCartUseCase: RemoveProductCartUseCase,
        createOrderUseCase: CreateOrderUseCase,
        deleteCartUseCase: DeleteCartUseCase
    ): AppViewModelFactory {
        return AppViewModelFactory(
            getProductsUseCase,
            addToCartUseCase,
            getCartUseCase,
            removeProductCartUseCase,
            createOrderUseCase,
            deleteCartUseCase
        )
    }

}