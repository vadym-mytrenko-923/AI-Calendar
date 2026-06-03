package com.agents.app.demo.di.products

import com.agents.app.demo.data.features.products.ProductsRepositoryImpl
import com.agents.app.demo.data.features.products.remote.source.ProductsRemoteDataSource
import com.agents.app.demo.data.features.products.remote.source.ProductsRemoteDataSourceImpl
import com.agents.app.demo.domain.features.product.ProductsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ProductsModule {
    @Provides
    @Singleton
    fun provideProductsRemoteDataSource(remoteDataSourceImpl: ProductsRemoteDataSourceImpl): ProductsRemoteDataSource {
        return remoteDataSourceImpl
    }

    @Provides
    @Singleton
    fun provideProductsRepository(repositoryImpl: ProductsRepositoryImpl): ProductsRepository = repositoryImpl
}
