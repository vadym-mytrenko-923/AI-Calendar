package com.agents.app.demo.data.features.products.remote.api

import com.agents.app.demo.data.features.products.remote.model.ProductsResponse
import retrofit2.http.GET

interface ProductsApi {
    @GET("products")
    suspend fun getProducts(): ProductsResponse
}
