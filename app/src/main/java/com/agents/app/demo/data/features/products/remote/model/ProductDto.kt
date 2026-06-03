package com.agents.app.demo.data.features.products.remote.model

import com.google.gson.annotations.SerializedName

data class ProductDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("category")
    val category: ProductCategoryDto,
    @SerializedName("price")
    val price: Float,
)
