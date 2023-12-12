package com.example.mviwithcomposeexample.data.repository

import com.example.mviwithcomposeexample.data.model.ProductRequestModel
import com.example.mviwithcomposeexample.data.model.ProductResponseModel
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getProducts(): Flow<List<ProductResponseModel>>

    fun addProduct(product: ProductRequestModel): Flow<Boolean>

    fun deleteProduct(id: Int): Flow<Boolean>

    fun updateProduct(id: Int, product: ProductRequestModel): Flow<Boolean>

    fun getProductById(id: Int): Flow<ProductResponseModel?>

    fun updateProductTotal(id: Int, total: Int): Flow<Boolean>
}