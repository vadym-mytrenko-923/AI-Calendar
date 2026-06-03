package com.agents.app.demo.data.features.products

import com.agents.app.demo.data.features.products.mapper.toDomainModels
import com.agents.app.demo.data.features.products.remote.source.ProductsRemoteDataSource
import com.agents.app.demo.domain.features.product.ProductsRepository
import com.agents.app.demo.domain.features.product.model.Product
import javax.inject.Inject

class ProductsRepositoryImpl @Inject constructor(
    private val remoteDataSource: ProductsRemoteDataSource,
) : ProductsRepository {
    override suspend fun getProducts(): List<Product> {
        return remoteDataSource.getProducts().products.toDomainModels()
    }
}
