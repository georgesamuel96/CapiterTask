package com.georgesamuel.capitertask.data

import com.georgesamuel.capitertask.model.OrderRequest
import com.georgesamuel.capitertask.model.ProductDetails
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface APIService {

    @GET("products")
    fun getProducts(@Query("q") query: String): Observable<List<ProductDetails>>

    @POST("orders")
    fun orderNow(@Body order: List<OrderRequest>): Observable<Unit>
}