package com.georgesamuel.capitertask.data

import com.georgesamuel.capitertask.model.ProductDetails
import io.reactivex.Flowable

interface LocalDataSource {
    fun saveProduct(product : ProductDetails): Long

    fun getProducts(): Flowable<List<ProductDetails>>

    fun getProduct(id: String): Flowable<ProductDetails>

    fun deleteProduct(productDetails: ProductDetails): Int

    fun deleteCart()
}