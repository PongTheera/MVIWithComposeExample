package com.example.mviwithcomposeexample.domain.mapper

import com.example.mviwithcomposeexample.data.model.ProductResponseModel
import com.example.mviwithcomposeexample.domain.model.ProductModel

class ProductToDomainMapper() {

    fun transform(product: ProductResponseModel): ProductModel {
        return ProductModel(
            id = product.id,
            name = product.name,
            price = product.price,
            description = product.description,
            formattedName = formatName(product.name),
            formattedPrice = formatPrice(product.price),
            formattedDescription = formatDescription(product.description),
            total = product.total
        )
    }

    private fun formatName(name: String): String {
        return "Name : $name"
    }

    private fun formatPrice(price: Double): String {
        return "$${String.format("%.0f", price)}"
    }

    private fun formatDescription(description: String): String {
        return "Description : $description"
    }

}