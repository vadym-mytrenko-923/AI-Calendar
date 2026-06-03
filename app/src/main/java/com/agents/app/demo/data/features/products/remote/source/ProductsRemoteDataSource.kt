package com.agents.app.demo.data.features.products.remote.source

import com.agents.app.demo.data.features.products.remote.model.ProductsResponse

interface ProductsRemoteDataSource {
    suspend fun getProducts(): ProductsResponse
}
