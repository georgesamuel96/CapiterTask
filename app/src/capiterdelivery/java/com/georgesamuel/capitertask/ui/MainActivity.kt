package com.georgesamuel.capitertask.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.georgesamuel.capitertask.R
import com.georgesamuel.capitertask.Utils
import com.georgesamuel.capitertask.di.Injector
import com.georgesamuel.capitertask.viewmodel.AppViewModel
import com.georgesamuel.capitertask.viewmodel.AppViewModelFactory
import kotlinx.android.synthetic.capiterdelivery.activity_main.*
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
            this@MainActivity
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Utils.setLocale(this, "ar")
        setContentView(R.layout.activity_main)

        supportActionBar?.title = getString(R.string.orders)

        (application as Injector).createAppSubComponent().inject(this)

        appViewModel.getOrders()

        initList()
        observeViewModel()
    }

    private fun observeViewModel() {
        appViewModel.apply {
            ordersLiveData.observe(this@MainActivity, {
                updateOrdersListWithGrouping(it)
            })
            groupedOrdersLiveData.observe(this@MainActivity,{
                productsAdapter.clear()
                productsAdapter.addNewList(it)
            })
            successFullAddToCartLiveData.observe(this@MainActivity, {
                if (it)
                    productsAdapter.updateProductAt(this.addToCartProductPosition)
                else
                    addToCartProductPosition = -1
            })
        }
    }

    private fun initList() {
        rvOrders.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rvOrders.setHasFixedSize(true)
        rvOrders.adapter = productsAdapter
    }

    override fun onDestroy() {
        super.onDestroy()

        appViewModel.clear()
    }

    companion object {
        const val EXTRA_REFRESH_HOME = "EXTRA_REFRESH_HOME"
    }
}