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
import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
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
    private val deleteCartUseCase: DeleteCartUseCase
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

    var addToCartProductPosition = -1
    var removeFromCartPosition = -1

    fun getProducts(pageNumber: Int, cartItems: Map<String, Int>) {
        productsMutableList.clear()
        val observableList =
            getProductsUseCase.execute(PageNumberQuery(pageNumber).toString())

        compositeDisposable.add(
            observableList
                .flatMapIterable { products -> products }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { product ->
                    product.count = cartItems[product.id] ?: 0
                    return@map product
                }
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

    fun addToCart(productDetails: ProductDetails, position: Int) {
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

    fun removeFromCart(productDetails: ProductDetails, position: Int) {
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

    fun orderNow(productsList: List<ProductDetails>, orderName: String){
        if(orderName.isEmpty())
            return
        val newList = mutableListOf<OrderRequest>()
        productsList.forEach {
            newList.add(OrderRequest(it.id, orderName, it.imageUrl, it.name, it.price, it.count))
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

    fun deleteCart(){

        Completable.fromAction { deleteCartUseCase.execute() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
            }
    }

    private fun getCartAsMap(list: List<ProductDetails>): Map<String, Int>{
        val map = mutableMapOf<String, Int>()
        list.forEach { map[it.id] = it.count }
        return map
    }

    fun clear() {
        compositeDisposable.clear()
    }
}