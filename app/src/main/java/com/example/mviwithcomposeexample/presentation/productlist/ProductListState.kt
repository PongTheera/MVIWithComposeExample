package com.example.mviwithcomposeexample.presentation.productlist

import com.example.mviwithcomposeexample.domain.model.ProductModel

sealed interface ProductListUiState {

    //show loading the first time calling the API, example the skeleton loading
    object ProductLoading : ProductListUiState

    //show a load product error on the screen
    data class ProductError(val error: Throwable?) : ProductListUiState

    //show all the product lists on the screen.
    data class Success(val productList: List<ProductModel>) : ProductListUiState

    //show an empty product message on the screen
    object EmptyProductList : ProductListUiState

}

sealed interface ProductListEffect {

    //initial state
    object Idle : ProductListEffect

    //when an action is taken, like adding, deleting, or updating the product, the loading progress bar is shown.
    object ProcessLoading : ProductListEffect

    //when an action like adding, deleting, or updating is successful, hide the dialog. and show the toast message.
    data class ProcessSuccess(val operationType: OperationType) : ProductListEffect

    //when an action is an error, it will show a toast message and then hide the dialog.
    data class ProcessError(val error: Throwable) : ProductListEffect

}

enum class OperationType {
    ADD_PRODUCT,
    DELETE_PRODUCT,
    UPDATE_PRODUCT_DETAIL,
    UPDATE_PRODUCT_NUMBER
}