package com.georgesamuel.capitertask.usecases

import androidx.lifecycle.MutableLiveData
import com.georgesamuel.capitertask.data.LocalDataSource
import com.georgesamuel.capitertask.model.ProductDetails
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.schedulers.Schedulers

class AddToCartUseCase(private val localDataSource: LocalDataSource) {

    fun execute(productDetails: ProductDetails): Completable {

        return Completable
            .fromAction(object : Action {
                override fun run() {
                    localDataSource.saveProduct(productDetails)
                }
            })
    }
}