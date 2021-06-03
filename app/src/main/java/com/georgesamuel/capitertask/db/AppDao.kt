package com.georgesamuel.capitertask.db

import androidx.room.*
import com.georgesamuel.capitertask.model.ProductDetails
import io.reactivex.Flowable

@Dao
interface AppDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveProduct(product : ProductDetails): Long

    @Query("DELETE FROM ProductDetails")
    fun deleteAllProducts()

    @Query("SELECT * FROM ProductDetails")
    fun getProducts():Flowable<List<ProductDetails>>

    @Query("SELECT * FROM ProductDetails WHERE id=:id")
    fun getProduct(id: String):Flowable<ProductDetails>

    @Delete
    fun deleteProduct(productDetails: ProductDetails): Int
}