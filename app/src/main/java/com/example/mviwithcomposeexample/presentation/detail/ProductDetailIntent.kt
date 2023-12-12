package com.example.mviwithcomposeexample.presentation.detail

import com.example.mviwithcomposeexample.domain.model.ProductOperator

sealed interface ProductDetailIntent {

    data class FetchProductDetail(val productId: String) : ProductDetailIntent

    data class UpdateProductQuantity(val productId: Int, val productOperator: ProductOperator) : ProductDetailIntent

}