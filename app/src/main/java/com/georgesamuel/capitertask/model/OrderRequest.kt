package com.georgesamuel.capitertask.model


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class OrderRequest(

    @SerializedName("_id")
    var id: String? = null,
    @SerializedName("product-id")
    var productId: String = "",
    @SerializedName("order-name")
    var orderName: String = "",
    @SerializedName("product-image-url")
    var productImageUrl: String = "",
    @SerializedName("product-name")
    var productName: String = "",
    @SerializedName("product-price")
    var productPrice: Int = 0,
    @SerializedName("product-quantity")
    var productQuantity: Int = 0
)