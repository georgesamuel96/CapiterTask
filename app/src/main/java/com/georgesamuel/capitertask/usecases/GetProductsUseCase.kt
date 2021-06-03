package com.georgesamuel.capitertask.usecases

import androidx.lifecycle.MutableLiveData
import com.georgesamuel.capitertask.data.AppRepository
import com.georgesamuel.capitertask.model.ProductDetails
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable

class GetProductsUseCase(
    private val appRepository: AppRepository,
) {

    fun execute(pageNumber: String): Observable<List<ProductDetails>>{
        return appRepository.getProducts(pageNumber)
    }
}