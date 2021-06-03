package com.georgesamuel.capitertask.usecases

import com.georgesamuel.capitertask.data.LocalDataSource

class DeleteCartUseCase(private val localDataSource: LocalDataSource) {

    fun execute() = localDataSource.deleteCart()
}