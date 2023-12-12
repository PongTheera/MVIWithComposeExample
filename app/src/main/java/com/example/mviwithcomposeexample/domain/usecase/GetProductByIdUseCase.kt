package com.example.mviwithcomposeexample.domain.usecase

import com.example.mviwithcomposeexample.data.repository.ProductRepository
import com.example.mviwithcomposeexample.domain.mapper.ProductToDomainMapper
import com.example.mviwithcomposeexample.domain.model.ProductModel
import com.example.mviwithcomposeexample.domain.model.ResultState
import com.example.mviwithcomposeexample.domain.model.asResultState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

interface GetProductByIdUseCase {
    operator fun invoke(productId: String): Flow<ProductModel?>
}

class GetProductByIdUseCaseImpl(
    private val productRepository: ProductRepository,
    private val productToDomainMapper: ProductToDomainMapper,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : GetProductByIdUseCase {

    override fun invoke(productId: String): Flow<ProductModel?> {
        return productRepository.getProductById(id = productId.toInt())
            .map { product ->
                if (product == null) null
                else productToDomainMapper.transform(product)
            }
            .flowOn(dispatcher)
    }

}