package com.georgesamuel.capitertask.usecases

import com.georgesamuel.capitertask.data.AppRepository
import com.georgesamuel.capitertask.model.OrderRequest
import io.reactivex.Observable

class CreateOrderUseCase(
    private val appRepository: AppRepository,
) {

    fun execute(list: List<OrderRequest>): Observable<Unit> {
        return appRepository.orderNow(list)
    }
}