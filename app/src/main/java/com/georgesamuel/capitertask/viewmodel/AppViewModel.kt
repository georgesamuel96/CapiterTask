package com.georgesamuel.capitertask.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.georgesamuel.capitertask.model.OrderRequest
import com.georgesamuel.capitertask.model.PageNumberQuery
import com.georgesamuel.capitertask.model.ProductDetails
import com.georgesamuel.capitertask.usecases.*
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class AppViewModel(
    private val getProductsUseCase: GetProductsUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val getCartUseCase: GetCartUseCase,
    private val removeProductCartUseCase: RemoveProductCartUseCase,
    private val createOrderUseCase: CreateOrderUseCase,
    private val deleteCartUseCase: DeleteCartUseCase,
    private val getProductFromLocalUseCase: GetProductFromLocalUseCase,
    private val getOrdersUseCase: GetOrdersUseCase
) : ViewModel() {

    private val TAG = AppViewModel::class.java.simpleName
    private val compositeDisposable = CompositeDisposable()

    val productsMutableList = mutableListOf<ProductDetails>()
    private val productsList = MutableLiveData<List<ProductDetails>>()
    val productsListLiveData: LiveData<List<ProductDetails>>
        get() = productsList

    private val successFullAddToCart = MutableLiveData<Boolean>()
    val successFullAddToCartLiveData: LiveData<Boolean>
        get() = successFullAddToCart

    private val successRemoveFromCart = MutableLiveData<Boolean>()
    val successRemoveFromCartLiveData: LiveData<Boolean>
        get() = successRemoveFromCart

    private val cartProducts = MutableLiveData<List<ProductDetails>>()
    val cartProductsLiveData: LiveData<List<ProductDetails>>
        get() = cartProducts

    private val successfulCreatedOrder = MutableLiveData<Boolean>()
    val successfulCreatedOrderLiveData: LiveData<Boolean>
        get() = successfulCreatedOrder

    private val ordersData = mutableListOf<OrderRequest>()
    private val ordersList = MutableLiveData<List<OrderRequest>>()
    val ordersLiveData: LiveData<List<OrderRequest>>
        get() = ordersList

    private val groupedOrdersList = MutableLiveData<List<OrderRequest>>()
    val groupedOrdersLiveData: LiveData<List<OrderRequest>>
    get() = groupedOrdersList

    var addToCartProductPosition = -1
    var removeFromCartPosition = -1

    fun getProductsFromServer(pageNumber: Int) {
        productsMutableList.clear()
        val observableList =
            getProductsUseCase.execute(PageNumberQuery(pageNumber).toString())

        compositeDisposable.add(
            observableList
                .flatMapIterable { products ->
                    products
                }
                .map { product ->
                    product.count = getProductFromLocalUseCase.execute(product.id)
                    return@map product
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<ProductDetails>() {
                    override fun onNext(product: ProductDetails) {
                        productsMutableList.add(product)
                    }

                    override fun onError(e: Throwable) {
                        Log.e(TAG, e.message.toString())
                    }

                    override fun onComplete() {
                        productsList.value = productsMutableList
                    }

                })
        )
    }

    fun addProductToCart(productDetails: ProductDetails, position: Int) {
        addToCartProductPosition = position
        val observable = addToCartUseCase.execute(productDetails)

        compositeDisposable.add(
            observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableCompletableObserver() {
                    override fun onComplete() {
                        successFullAddToCart.value = true
                    }

                    override fun onError(e: Throwable) {
                        successFullAddToCart.value = false
                    }
                })
        )
    }

    fun removeProductFromCart(productDetails: ProductDetails, position: Int) {
        removeFromCartPosition = position
        val observable = removeProductCartUseCase.execute(productDetails)

        compositeDisposable.add(
            observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableCompletableObserver() {
                    override fun onComplete() {
                        successRemoveFromCart.value = true
                    }

                    override fun onError(e: Throwable) {
                        successRemoveFromCart.value = false
                    }
                })
        )
    }

    fun getCartProducts() {
        val observable = getCartUseCase.execute()

        compositeDisposable.add(
            observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Consumer<List<ProductDetails>> {
                    override fun accept(t: List<ProductDetails>?) {
                        cartProducts.value = t ?: ArrayList()
                    }
                })
        )
    }

    fun orderNow(productsList: List<ProductDetails>, orderName: String) {
        if (orderName.isEmpty())
            return
        val newList = mutableListOf<OrderRequest>()
        productsList.forEach {
            newList.add(
                OrderRequest(
                    productId = it.id,
                    productName = it.name,
                    productImageUrl = it.imageUrl,
                    orderName = orderName,
                    productPrice = it.price,
                    productQuantity = it.count
                )
            )
        }

        val observableList =
            createOrderUseCase.execute(newList)

        compositeDisposable.add(
            observableList
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<Unit>() {
                    override fun onNext(t: Unit) {

                    }

                    override fun onError(e: Throwable) {
                        successfulCreatedOrder.value = false
                    }

                    override fun onComplete() {
                        successfulCreatedOrder.value = true
                    }
                })
        )

    }

    fun getOrders() {
        val observableList =
            getOrdersUseCase.execute()

        compositeDisposable.add(
            observableList
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<List<OrderRequest>>() {
                    override fun onNext(t: List<OrderRequest>) {
                        ordersData.clear()
                        ordersData.addAll(t)
                    }

                    override fun onError(e: Throwable) {
                    }

                    override fun onComplete() {
                        ordersList.value = ordersData
                    }


                })
        )
    }

    private fun groupOrdersList(orders: List<OrderRequest>): List<OrderRequest>{
        var lastOrderName = ""
        val newList = mutableListOf<OrderRequest>()

        orders.forEach {
            if(it.orderName != lastOrderName) {
                newList.add(OrderRequest("", orderName = it.orderName))
                lastOrderName = it.orderName
            }
            newList.add(it)
        }
        return newList
    }

    fun updateOrdersListWithGrouping(orders: List<OrderRequest>) {
        compositeDisposable.add(
            Single.fromCallable {
                groupOrdersList(orders)
            }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Consumer<List<OrderRequest>> {
                    override fun accept(t: List<OrderRequest>?) {
                        groupedOrdersList.value = t?: ArrayList()
                    }
                })
        )
    }

    private fun doTask() {

    }

    fun deleteCart() {

        Completable.fromAction { deleteCartUseCase.execute() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
            }
    }

    fun clear() {
        compositeDisposable.clear()
    }
}