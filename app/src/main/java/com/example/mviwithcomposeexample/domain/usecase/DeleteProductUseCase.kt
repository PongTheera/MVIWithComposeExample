package com.example.mviwithcomposeexample.domain.usecase

import com.example.mviwithcomposeexample.data.repository.ProductRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

interface DeleteProductUseCase {
    operator fun invoke(id: Int): Flow<Boolean>
}

class DeleteProductUseCaseImpl(
    private val productRepository: ProductRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : DeleteProductUseCase {

    override fun invoke(id: Int): Flow<Boolean> {
        return productRepository.deleteProduct(id = id)
            .flowOn(dispatcher)
    }

}
