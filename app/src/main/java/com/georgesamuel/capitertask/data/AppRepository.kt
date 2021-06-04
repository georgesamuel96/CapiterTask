package com.georgesamuel.capitertask.data

import com.georgesamuel.capitertask.model.OrderRequest
import com.georgesamuel.capitertask.model.ProductDetails
import io.reactivex.Observable

interface AppRepository {

    fun getProducts(pageNumber: String): Observable<List<ProductDetails>>

    fun orderNow(list: List<OrderRequest>): Observable<Unit>

    fun getOrders(): Observable<List<OrderRequest>>
}