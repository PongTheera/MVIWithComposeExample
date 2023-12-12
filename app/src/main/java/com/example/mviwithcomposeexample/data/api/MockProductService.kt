package com.example.mviwithcomposeexample.data.api

import android.util.Log
import com.example.mviwithcomposeexample.data.model.ProductRequestModel
import com.example.mviwithcomposeexample.data.model.ProductResponseModel

class MockProductService : ProductService {

    private val productList = mutableListOf<ProductResponseModel>().apply {
        addAll(
            listOf(
                ProductResponseModel(id = 1, name = "Mango", 40.0, "Mango Detail", total = 0),
                ProductResponseModel(id = 2, name = "Mango Steen", 30.0, "Mango Steen Detail", total = 0),
//                ProductResponseModel(id = 3, name = "Apple", 20.0, "Apple Detail", total = 0),
//                ProductResponseModel(id = 4, name = "Banana", 50.0, "Banana Detail", total = 0),
//                ProductResponseModel(id = 5, name = "Orange", 60.0, "Orange Detail", total = 0),
            )
        )
    }

    override suspend fun getProducts(): List<ProductResponseModel> {
        return productList
    }

    override suspend fun addProduct(product: ProductRequestModel) {
        val lastId = if (productList.isEmpty()) 0 else productList.maxOf { it.id } + 1
        val model = ProductResponseModel(
            id = lastId,
            name = product.name,
            price = product.price,
            description = product.description,
            total = 0
        )
        productList.add(model)
    }

    override suspend fun deleteProduct(id: Int) {
        val product = getProductById(id = id)
        productList.remove(product)
    }

    override suspend fun updateProduct(id: Int, product: ProductRequestModel) {
        getProductById(id)?.let { _product ->
            val index = productList.indexOf(_product)
            val newProduct = ProductResponseModel(id = id, name = product.name, price = product.price, description = product.description, total = _product.total)
            productList.set(index, newProduct)
        }
    }

    override suspend fun getProductById(id: Int): ProductResponseModel? {
        return productList.find {
            it.id == id
        }
    }

    override suspend fun updateProductTotal(id: Int, total: Int) {
        getProductById(id)?.let { product ->
            val index = productList.indexOf(product)
            val newProduct = ProductResponseModel(id = id, name = product.name, price = product.price, description = product.description, total = total)
            productList.set(index, newProduct)
        }
    }
}