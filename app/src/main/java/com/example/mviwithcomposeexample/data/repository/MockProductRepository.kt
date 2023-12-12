package com.example.mviwithcomposeexample.data.repository

import com.example.mviwithcomposeexample.data.datasource.MockProductDataSource
import com.example.mviwithcomposeexample.data.model.ProductRequestModel
import com.example.mviwithcomposeexample.data.model.ProductResponseModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MockProductRepository(
    private val dataSource: MockProductDataSource,
) : ProductRepository {

    companion object {
        const val DELAY = 300L
    }

    override fun getProducts(): Flow<List<ProductResponseModel>> {
        return flow {
            delay(DELAY)
            val productList = dataSource.getProducts()
            emit(productList)
        }
    }

    override fun addProduct(product: ProductRequestModel): Flow<Boolean> {
        return flow {
            delay(DELAY)
            dataSource.addProduct(product = product)
            emit(true)
        }
    }

    override fun deleteProduct(id: Int): Flow<Boolean> {
        return flow {
            delay(DELAY)
            dataSource.deleteProduct(id = id)
            emit(true)
        }
    }

    override fun updateProduct(id: Int, product: ProductRequestModel): Flow<Boolean> {
        return flow {
            delay(DELAY)
            dataSource.updateProduct(id = id, product = product)
            emit(true)
        }
    }

    override fun getProductById(id: Int): Flow<ProductResponseModel?> {
        return flow {
            delay(DELAY)
            val product = dataSource.getProductById(id = id)
            emit(product)
        }
    }

    override fun updateProductTotal(id: Int, total: Int): Flow<Boolean> {
        return flow {
            delay(DELAY)
            dataSource.updateProductTotal(id = id, total = total)
            emit(true)
        }
    }

}