package com.georgesamuel.capitertask.usecases

import androidx.lifecycle.MutableLiveData
import com.georgesamuel.capitertask.data.LocalDataSource
import com.georgesamuel.capitertask.db.AppDao
import com.georgesamuel.capitertask.model.ProductDetails
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

class GetCartUseCase(private val localDataSource: LocalDataSource) {

    fun execute(): Flowable<List<ProductDetails>>{
        return localDataSource.getProducts()
    }
}