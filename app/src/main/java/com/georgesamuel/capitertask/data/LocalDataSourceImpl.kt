package com.georgesamuel.capitertask.data

import com.georgesamuel.capitertask.db.AppDao
import com.georgesamuel.capitertask.model.ProductDetails
import io.reactivex.Flowable

class LocalDataSourceImpl(private val appDao: AppDao): LocalDataSource {

    override fun saveProduct(product: ProductDetails) = appDao.saveProduct(product)

    override fun getProducts(): Flowable<List<ProductDetails>> {
        return appDao.getProducts()
    }

    override fun getProduct(id: String): Int {
        return appDao.getProduct(id)
    }

    override fun deleteProduct(productDetails: ProductDetails) = appDao.deleteProduct(productDetails)

    override fun deleteCart() = appDao.deleteAllProducts()
}