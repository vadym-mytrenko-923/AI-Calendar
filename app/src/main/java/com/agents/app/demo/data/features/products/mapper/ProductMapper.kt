package com.agents.app.demo.data.features.products.mapper

import com.agents.app.demo.data.features.products.remote.model.ProductCategoryDto
import com.agents.app.demo.data.features.products.remote.model.ProductDto
import com.agents.app.demo.domain.features.product.model.Product
import com.agents.app.demo.domain.features.product.model.ProductCategory

fun List<ProductDto>.toDomainModels(): List<Product> = this.map {
    it.toDomainModel()
}

fun ProductDto.toDomainModel(): Product = Product(
    id = this.id,
    title = this.title,
    description = this.description,
    category = this.category.toDomainModel(),
    price = this.price,
)

fun ProductCategoryDto.toDomainModel(): ProductCategory = ProductCategory.valueOf(this.name)
