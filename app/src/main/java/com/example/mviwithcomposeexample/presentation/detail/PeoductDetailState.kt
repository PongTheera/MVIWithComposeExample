package com.example.mviwithcomposeexample.presentation.detail

import com.example.mviwithcomposeexample.domain.model.ProductModel

sealed interface ProductDetailUiState {

    object Loading : ProductDetailUiState

    data class Success(val productModel: ProductModel?) : ProductDetailUiState

    object NotFoundProductUi : ProductDetailUiState

    data class Error(val message: String) : ProductDetailUiState

}

sealed interface ProductDetailEffect {

    object Idle : ProductDetailEffect

    object Loading : ProductDetailEffect

    object Success : ProductDetailEffect

    data class Error(val message: String) : ProductDetailEffect

}