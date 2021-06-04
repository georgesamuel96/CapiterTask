package com.georgesamuel.capitertask.usecases

import com.georgesamuel.capitertask.data.AppRepository

class GetOrdersUseCase(private val appRepository: AppRepository) {

    fun execute() = appRepository.getOrders()
}