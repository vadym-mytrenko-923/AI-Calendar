package com.agents.app.demo.domain.features.product

import com.agents.app.demo.domain.features.product.model.Product

interface ProductsRepository {
    suspend fun getProducts(): List<Product>
}
