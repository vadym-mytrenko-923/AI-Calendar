package com.agents.app.demo.domain.features.product.model

data class Product(
    val id: Int,
    val title: String,
    val description: String,
    val category: ProductCategory,
    val price: Float,
)
