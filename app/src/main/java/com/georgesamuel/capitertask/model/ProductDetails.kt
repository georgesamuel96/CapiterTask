package com.georgesamuel.capitertask.model
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class ProductDetails(
    @PrimaryKey
    @SerializedName("_id")
    var id: String,
    @SerializedName("image-url")
    var imageUrl: String,
    @SerializedName("name")
    var name: String,
    @SerializedName("page")
    var page: Int,
    @SerializedName("price")
    var price: Int,

    var count: Int
)