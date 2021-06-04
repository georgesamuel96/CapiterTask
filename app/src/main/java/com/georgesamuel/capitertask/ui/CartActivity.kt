package com.georgesamuel.capitertask.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.georgesamuel.capitertask.R
import com.georgesamuel.capitertask.Utils
import com.georgesamuel.capitertask.di.Injector
import com.georgesamuel.capitertask.model.ProductDetails
import com.georgesamuel.capitertask.viewmodel.AppViewModel
import com.georgesamuel.capitertask.viewmodel.AppViewModelFactory
import kotlinx.android.synthetic.main.activity_cart.*
import javax.inject.Inject

class CartActivity : AppCompatActivity() {

    @Inject
    lateinit var appViewModelFactory: AppViewModelFactory
    private val appViewModel: AppViewModel by lazy {
        ViewModelProvider(this, appViewModelFactory).get(AppViewModel::class.java)
    }

    private val productsAdapter: ProductsAdapter by lazy {
        ProductsAdapter(
            this@CartActivity,
            false,
            { product, position -> removeFromCartListener(product, position) },
            null
        )
    }
    private var refreshHome = false

    override fun onCreate(savedInstanceState: Bundle?) {
        Utils.setLocale(this, "ar")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        supportActionBar?.title = getString(R.string.products)

        (application as Injector).createAppSubComponent().inject(this)

        appViewModel.getCartProducts()

        initList()
        observeViewModel()
        setListeners()
    }

    private fun setListeners() {
        btnOrderNow.setOnClickListener {
            appViewModel.orderNow(productsAdapter.getProducts(), etOrderName.text.toString().trim())
        }

        btnBack.setOnClickListener {
            closeCartAndRefreshHome()
        }
    }

    private fun observeViewModel(){
        appViewModel.apply {
            cartProductsLiveData.observe(this@CartActivity, {
                productsAdapter.clear()
                productsAdapter.addNewList(it)
            })
            successRemoveFromCartLiveData.observe(this@CartActivity, {
                if(it)
                    productsAdapter.removeProductAt(this.removeFromCartPosition)
                else
                    removeFromCartPosition = -1
            })
            successfulCreatedOrderLiveData.observe(this@CartActivity, {
                if(it){
                    productsAdapter.clear()
                    tvCart.visibility = View.VISIBLE
                    btnOrderNow.visibility = View.GONE
                    btnBack.visibility = View.VISIBLE
                    deleteCart()
                    etOrderName.setText("")
                    refreshHome = true
                } else {
                    tvCart.visibility = View.GONE
                    btnOrderNow.visibility = View.VISIBLE
                    btnBack.visibility = View.GONE
                    refreshHome = false
                }
            })
        }
    }

    private fun removeFromCartListener(productDetails: ProductDetails, position: Int){
        appViewModel.removeProductFromCart(productDetails, position)
    }

    private fun initList() {
        rvCart.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rvCart.setHasFixedSize(true)
        rvCart.adapter = productsAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.back_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.item_back -> {
                closeCartAndRefreshHome()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun closeCartAndRefreshHome() {
        val intent = Intent()
        intent.putExtra(MainActivity.EXTRA_REFRESH_HOME, refreshHome)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()

        appViewModel.clear()
    }
}