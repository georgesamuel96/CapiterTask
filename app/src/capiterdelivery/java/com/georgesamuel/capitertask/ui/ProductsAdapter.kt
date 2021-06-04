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
import com.georgesamuel.capitertask.model.OrderRequest

class ProductsAdapter(
    private val context: Context
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val list: MutableList<OrderRequest> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if(viewType == OrderType.ORDER.value) {
            ProductsViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
            )
        } else {
            NameViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_name, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder.itemViewType){
            OrderType.ORDER.value -> {
                (holder as ProductsViewHolder).bind(
                    context, list[position]
                )
            }
            else -> {
                (holder as NameViewHolder).bind(
                    list[position]
                )
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if(list[position].id.isNullOrEmpty()) OrderType.HEADER.value else OrderType.ORDER.value
    }

    override fun getItemCount(): Int = list.size

    fun clear() {
        this.list.clear()
        this.notifyDataSetChanged()
    }

    fun addNewList(newList: List<OrderRequest>) {
        newList.forEach {
            addProduct(it)
        }
    }

    private fun addProduct(product: OrderRequest) {
        this.list.add(product)
        this.notifyItemInserted(list.size - 1)
    }

    fun updateProductAt(position: Int) {
        this.notifyItemChanged(position)
    }

    fun removeProductAt(position: Int) {
        this.list.removeAt(position)
        this.notifyItemRemoved(position)
        this.notifyItemRangeChanged(position, this.list.size)
    }

    fun getProducts() = this.list


    class ProductsViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val imgProduct = view.findViewById<ImageView>(R.id.imgProduct)
        private val tvProductName = view.findViewById<TextView>(R.id.tvProductName)
        private val tvProductPrice = view.findViewById<TextView>(R.id.tvProductPrice)
        private val tvCartCount = view.findViewById<TextView>(R.id.tvCartCount)

        fun bind(
            context: Context,
            product: OrderRequest
        ) {

            Glide.with(context).load(product.productImageUrl).fitCenter().into(imgProduct)
            tvProductName.text = product.productName
            tvProductPrice.text = "${product.productPrice} ${context.getString(R.string.pound)}"

            tvCartCount.text = "${product.productQuantity}"
        }
    }

    class NameViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val tvOrderName = view.findViewById<TextView>(R.id.tvOrderName)

        fun bind(
            product: OrderRequest
        ) {
            tvOrderName.text = product.orderName
        }
    }
}