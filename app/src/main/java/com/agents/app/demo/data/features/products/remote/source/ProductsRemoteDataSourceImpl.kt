package com.agents.app.demo.data.features.products.remote.source

import com.agents.app.demo.data.features.products.remote.api.ProductsApi
import com.agents.app.demo.data.features.products.remote.model.ProductsResponse
import javax.inject.Inject

class ProductsRemoteDataSourceImpl @Inject constructor(
    private val api: ProductsApi
) : ProductsRemoteDataSource {
    override suspend fun getProducts(): ProductsResponse = api.getProducts()
}
