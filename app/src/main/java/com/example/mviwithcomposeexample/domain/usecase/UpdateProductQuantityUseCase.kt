package com.example.mviwithcomposeexample.domain.usecase

import com.example.mviwithcomposeexample.data.repository.ProductRepository
import com.example.mviwithcomposeexample.domain.model.ProductOperator
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn

interface UpdateProductQuantityUseCase {
    operator fun invoke(productId: Int, productOperator: ProductOperator): Flow<Boolean>
}

@OptIn(ExperimentalCoroutinesApi::class)
class UpdateProductQuantityUseCaseImpl(
    private val productRepository: ProductRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : UpdateProductQuantityUseCase {

    override fun invoke(productId: Int, productOperator: ProductOperator): Flow<Boolean> {
        return productRepository.getProductById(id = productId)
            .flatMapLatest { product ->

                var newTotal = product?.total ?: 0

                when (productOperator) {
                    ProductOperator.ADD -> {
                        newTotal += 1
                    }

                    else -> {
                        newTotal -= 1
                    }
                }

                if (newTotal <= 0) newTotal = 0

                productRepository.updateProductTotal(id = productId, total = newTotal)

            }.flowOn(dispatcher)
    }

}