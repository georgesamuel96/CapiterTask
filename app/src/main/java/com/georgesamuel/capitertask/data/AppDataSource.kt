package com.georgesamuel.capitertask.data

import com.georgesamuel.capitertask.model.OrderRequest
import com.georgesamuel.capitertask.model.ProductDetails
import io.reactivex.Observable

class AppDataSource(private val apiService: APIService): AppRepository {

    override fun getProducts(pageNumber: String): Observable<List<ProductDetails>> = apiService.getProducts(pageNumber)

    override fun orderNow(list: List<OrderRequest>) = apiService.orderNow(list)

    override fun getOrders() =  apiService.getOrders()
}