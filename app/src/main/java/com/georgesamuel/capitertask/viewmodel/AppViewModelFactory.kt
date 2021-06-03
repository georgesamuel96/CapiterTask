package com.georgesamuel.capitertask.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.georgesamuel.capitertask.usecases.*

class AppViewModelFactory(
    private val getProductsUseCase: GetProductsUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val getCartUseCase: GetCartUseCase,
    private val removeProductCartUseCase: RemoveProductCartUseCase,
    private val createOrderUseCase: CreateOrderUseCase,
    private val deleteCartUseCase: DeleteCartUseCase
): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AppViewModel(
            getProductsUseCase,
            addToCartUseCase,
            getCartUseCase,
            removeProductCartUseCase,
            createOrderUseCase,
            deleteCartUseCase
        ) as T
    }
}