package com.agents.app.demo.ui.screens.product.mapper

import com.agents.app.demo.domain.features.product.model.Product
import com.agents.app.demo.ui.screens.product.model.ProductUiModel

fun List<Product>.toUiModels(): List<ProductUiModel> = this.map {
    it.toUiModel()
}

fun Product.toUiModel(): ProductUiModel = ProductUiModel(
    id = this.id,
    title = this.title,
    description = this.description,
    price = "$${this.price}",
)
