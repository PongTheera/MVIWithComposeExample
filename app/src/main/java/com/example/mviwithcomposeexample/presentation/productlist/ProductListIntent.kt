package com.example.mviwithcomposeexample.presentation.productlist

import com.example.mviwithcomposeexample.domain.model.ProductModel
import com.example.mviwithcomposeexample.domain.model.ProductOperator

sealed interface ProductListIntent {

    //user click the add button
    object AddProduct : ProductListIntent

    //user click the delete product item on the dialog
    data class DeleteProduct(val productId: Int) : ProductListIntent

    //load the product from the API
    object FetchProducts : ProductListIntent

    //show dialog
    data class ProductActionDialog(val product: ProductModel) : ProductListIntent

    //update the product detail
    data class UpdateProduct(val productId: Int) : ProductListIntent

    //update the quantity of product
    data class UpdateProductQuantity(val productId: Int, val productOperator: ProductOperator) : ProductListIntent

    //test the mock error case when fectching the product
    object FetchProductsWithError : ProductListIntent

}