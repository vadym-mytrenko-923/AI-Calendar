package com.agents.app.demo.domain.features.product.usecase

import com.agents.app.demo.domain.base.result.useResultWrapper
import com.agents.app.demo.domain.base.usecase.BaseNoParamsUseCase
import com.agents.app.demo.domain.features.product.ProductsRepository
import com.agents.app.demo.domain.features.product.model.Product
import dagger.Reusable
import javax.inject.Inject

@Reusable
class GetProductsUseCase @Inject constructor(
    private val productsRepository: ProductsRepository
) : BaseNoParamsUseCase<Result<List<Product>>>() {
    override suspend fun execute(): Result<List<Product>> = useResultWrapper {
        productsRepository.getProducts()
    }
}
