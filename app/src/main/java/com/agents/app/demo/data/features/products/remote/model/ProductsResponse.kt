package com.agents.app.demo.data.features.products.remote.model

import com.google.gson.annotations.SerializedName

data class ProductsResponse(
    @SerializedName("products")
    val products: List<ProductDto>
)
