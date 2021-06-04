package com.georgesamuel.capitertask.usecases

import com.georgesamuel.capitertask.data.LocalDataSource

class GetProductFromLocalUseCase(private val localDataSource: LocalDataSource) {

    fun execute(id: String): Int = localDataSource.getProduct(id)
}