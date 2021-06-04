package com.georgesamuel.capitertask.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.georgesamuel.capitertask.R
import com.georgesamuel.capitertask.model.ProductDetails
import com.google.android.material.button.MaterialButton

class ProductsAdapter(
    private val context: Context,
    private val isCartView: Boolean,
    private val addToCartListener: (ProductDetails, Int) -> Unit,
    private val listRefreshedWithZeroCounts: (() -> Unit)?
    ): RecyclerView.Adapter<ProductsAdapter.ProductsViewHolder>() {

    private val list: MutableList<ProductDetails> = ArrayList()
    private var setCountsZero = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
        return ProductsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) {
        holder.bind(
            context,
            list[position],
            position,
            position == this.list.size - 1,
            isCartView,
            setCountsZero,
            addToCartListener,
            listRefreshedWithZeroCounts
        )
    }

    override fun getItemCount(): Int = list.size

    fun clear(){
        this.list.clear()
        this.notifyDataSetChanged()
    }

    fun addNewList(newList: List<ProductDetails>){
        newList.forEach {
            addProduct(it)
        }
    }

    private fun addProduct(product: ProductDetails){
        this.list.add(product)
        this.notifyItemInserted(list.size - 1)
    }

    fun updateProductAt(position: Int){
        this.notifyItemChanged(position)
    }

    fun removeProductAt(position: Int){
        this.list.removeAt(position)
        this.notifyItemRemoved(position)
        this.notifyItemRangeChanged(position, this.list.size)
    }

    fun getProducts() = this.list

    fun clearProductCount(){
        setCountsZero = true
        this.notifyDataSetChanged()
    }

    fun resetCountsZero(){
        this.setCountsZero = false
    }

    class ProductsViewHolder(view: View): RecyclerView.ViewHolder(view){

        private val imgProduct = view.findViewById<ImageView>(R.id.imgProduct)
        private val btnRemove = view.findViewById<ImageView>(R.id.btnRemove)
        private val tvProductName = view.findViewById<TextView>(R.id.tvProductName)
        private val tvProductPrice = view.findViewById<TextView>(R.id.tvProductPrice)
        private val tvCartCount = view.findViewById<TextView>(R.id.tvCartCount)
        private val btnAddToCart = view.findViewById<MaterialButton>(R.id.btnAddToCart)

        fun bind(
            context: Context,
            product: ProductDetails,
            position: Int,
            isLastPosition: Boolean,
            isCartView: Boolean,
            setCountsZero: Boolean,
            addToCartListener: (ProductDetails, Int) -> Unit,
            listRefreshedWithZeroCounts: (() -> Unit)?
        ){
            btnRemove.visibility = View.GONE
            btnAddToCart.visibility = View.GONE
            if(isCartView){
                btnRemove.visibility = View.VISIBLE
                btnAddToCart.visibility = View.GONE
            } else {
                btnRemove.visibility = View.GONE
                btnAddToCart.visibility = View.VISIBLE
            }
            Glide.with(context).load(product.imageUrl).fitCenter().into(imgProduct)
            tvProductName.text = product.name
            tvProductPrice.text = "${product.price}"

            if(setCountsZero) {
                product.count = 0
                if (isLastPosition)
                    listRefreshedWithZeroCounts?.invoke()
            }

            tvCartCount.text = "${product.count}"

            btnAddToCart.setOnClickListener {
                product.count++
                addToCartListener(product, position)
            }

            btnRemove.setOnClickListener {
                addToCartListener(product, position)
            }
        }
    }
}