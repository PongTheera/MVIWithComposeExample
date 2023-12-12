package com.example.mviwithcomposeexample.domain.usecase

import com.example.mviwithcomposeexample.data.model.ProductRequestModel
import com.example.mviwithcomposeexample.data.repository.ProductRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

interface UpdateProductUseCase {
    operator fun invoke(productId: Int): Flow<Boolean>
}

class UpdateProductUseCaseImpl(
    private val productRepository: ProductRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : UpdateProductUseCase {

    companion object {
        val randomFruitList = listOf<String>("Water Melon", "Strawberry", "Blueberry", "Pineapple")
    }

    private val format = SimpleDateFormat("ddMMMyyyy_HHmmss", Locale.ENGLISH)

    override operator fun invoke(productId: Int): Flow<Boolean> {

        val currentDateTime = format.format(Date())

        val randomPrice = (20..100).random().toDouble()
        val randomFruiteIndex = (0..3).random()
        val randomFruitName = randomFruitList.getOrNull(randomFruiteIndex) ?: randomFruitList[0]

        val randomMockProduct = ProductRequestModel(
            name = randomFruitName.plus("_$currentDateTime"),
            price = randomPrice,
            description = "update product at : $currentDateTime",
        )

        return productRepository.updateProduct(id = productId, product = randomMockProduct)
            .flowOn(dispatcher)

    }

}