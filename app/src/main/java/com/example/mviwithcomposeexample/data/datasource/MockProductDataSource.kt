package com.example.mviwithcomposeexample.data.datasource

import com.example.mviwithcomposeexample.data.api.ProductService
import com.example.mviwithcomposeexample.data.model.ProductRequestModel
import com.example.mviwithcomposeexample.data.model.ProductResponseModel


interface MockProductDataSource {
    suspend fun getProducts(): List<ProductResponseModel>

    suspend fun addProduct(product: ProductRequestModel)

    suspend fun deleteProduct(id: Int)

    suspend fun updateProduct(id: Int, product: ProductRequestModel)

    suspend fun getProductById(id: Int): ProductResponseModel?

    suspend fun updateProductTotal(id: Int, total: Int)
}

class MockProductDataSourceImpl(
    private val productService: ProductService
) : MockProductDataSource {

    override suspend fun getProducts(): List<ProductResponseModel> {
        return productService.getProducts()
    }

    override suspend fun addProduct(product: ProductRequestModel) {
        return productService.addProduct(product)
    }

    override suspend fun deleteProduct(id: Int) {
        return productService.deleteProduct(id)
    }

    override suspend fun updateProduct(id: Int, product: ProductRequestModel) {
        return productService.updateProduct(id, product)
    }

    override suspend fun getProductById(id: Int): ProductResponseModel? {
        return productService.getProductById(id)
    }

    override suspend fun updateProductTotal(id: Int, total: Int) {
        return productService.updateProductTotal(id, total)
    }

}