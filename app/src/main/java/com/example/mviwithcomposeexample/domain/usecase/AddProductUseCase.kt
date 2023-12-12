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

interface AddProductUseCase {
    operator fun invoke(): Flow<Boolean>
}

class AddProductUseCaseImpl(
    private val productRepository: ProductRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : AddProductUseCase {

    private companion object {
        val randomFruitList = listOf<String>("Tomato", "Apple", "Melon", "Orange")
    }

    private val format = SimpleDateFormat("ddMMMyyyy_HHmmss", Locale.ENGLISH)

    override fun invoke(): Flow<Boolean> {

        val currentDateTime = format.format(Date())

        val randomPrice = (20..100).random().toDouble()
        val randomFruiteIndex = (0..3).random()
        val randomFruitName = randomFruitList.getOrNull(randomFruiteIndex) ?: randomFruitList[0]

        val randomMockProduct = ProductRequestModel(
            name = randomFruitName.plus("_$currentDateTime"),
            price = randomPrice,
            description = "add product at : $currentDateTime",
        )

        return productRepository.addProduct(randomMockProduct)
            .flowOn(dispatcher)

    }

}