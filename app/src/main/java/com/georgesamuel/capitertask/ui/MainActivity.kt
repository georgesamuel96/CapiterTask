package com.georgesamuel.capitertask.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.georgesamuel.capitertask.R
import com.georgesamuel.capitertask.Utils
import com.georgesamuel.capitertask.di.Injector
import com.georgesamuel.capitertask.model.ProductDetails
import com.georgesamuel.capitertask.viewmodel.AppViewModel
import com.georgesamuel.capitertask.viewmodel.AppViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var appViewModelFactory: AppViewModelFactory
    private val appViewModel: AppViewModel by lazy {
        ViewModelProvider(this, appViewModelFactory).get(AppViewModel::class.java)
    }

    private val productsAdapter: ProductsAdapter by lazy {
        ProductsAdapter(
            this@MainActivity,
            false,
            { product, position -> addToCartListener(product, position) }
        ) { listRefreshedWithZeroCounts() }
    }

    private val startNewActivityWithResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.let {
                val refreshList = it.getBooleanExtra(EXTRA_REFRESH_HOME, false)
                if(refreshList)
                    productsAdapter.clearProductCount()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Utils.setLocale(this, "ar")
        setContentView(R.layout.activity_main)

        supportActionBar?.title = getString(R.string.products)

        (application as Injector).createAppSubComponent().inject(this)

        appViewModel.getProductsFromServer(1)

        initList()
        observeViewModel()
    }

    private fun observeViewModel(){
        appViewModel.apply {
            productsListLiveData.observe(this@MainActivity, {
                productsAdapter.clear()
                productsAdapter.addNewList(it)
            })
            successFullAddToCartLiveData.observe(this@MainActivity, {
                if(it)
                    productsAdapter.updateProductAt(this.addToCartProductPosition)
                else
                    addToCartProductPosition = -1
            })
        }
    }

    private fun addToCartListener(productDetails: ProductDetails, position: Int){
        appViewModel.addProductToCart(productDetails, position)
    }

    private fun listRefreshedWithZeroCounts(){
        productsAdapter.resetCountsZero()
    }

    private fun initList() {
        rvProducts.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rvProducts.setHasFixedSize(true)
        rvProducts.adapter = productsAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.cart_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.home -> {

                true
            }
            R.id.item_cart -> {
                startNewActivityWithResult.launch(Intent(this@MainActivity, CartActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        appViewModel.clear()
    }

    companion object{
        const val EXTRA_REFRESH_HOME = "EXTRA_REFRESH_HOME"
    }
}