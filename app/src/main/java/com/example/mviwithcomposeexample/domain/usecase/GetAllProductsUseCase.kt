package com.example.mviwithcomposeexample.domain.usecase

import com.example.mviwithcomposeexample.data.model.ProductResponseModel
import com.example.mviwithcomposeexample.data.repository.ProductRepository
import com.example.mviwithcomposeexample.domain.mapper.ProductToDomainMapper
import com.example.mviwithcomposeexample.domain.model.ProductModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

interface GetAllProductsUseCase {
    operator fun invoke(): Flow<List<ProductModel>>
}

class GetAllProductsUseCaseImpl(
    private val productRepository: ProductRepository,
    private val productToDomainMapper: ProductToDomainMapper,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : GetAllProductsUseCase {

    override fun invoke(): Flow<List<ProductModel>> {
        return productRepository.getProducts()
            .map(::mapToDomain)
            .flowOn(dispatcher)
    }

    private fun mapToDomain(productList: List<ProductResponseModel>): List<ProductModel> {
        return productList.map { product ->
            productToDomainMapper.transform(product)
        }
    }

}