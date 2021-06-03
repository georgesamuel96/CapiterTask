package com.georgesamuel.capitertask.usecases

import com.georgesamuel.capitertask.data.LocalDataSource
import com.georgesamuel.capitertask.model.ProductDetails
import io.reactivex.Completable
import io.reactivex.functions.Action

class RemoveProductCartUseCase(private val localDataSource: LocalDataSource) {

    fun execute(productDetails: ProductDetails): Completable {

        return Completable
            .fromAction(object : Action {
                override fun run() {
                    localDataSource.deleteProduct(productDetails)
                }
            })
    }
}