package com.example.mviwithcomposeexample.di

import com.example.mviwithcomposeexample.presentation.detail.ProductDetailViewModel
import com.example.mviwithcomposeexample.presentation.productlist.ProductListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {

    viewModel {
        ProductListViewModel(
            getAllProductsUseCase = get(),
            addProductUseCase = get(),
            deleteProductUseCase = get(),
            updateProductUseCase = get(),
            updateProductQuantityUseCase = get()
        )
    }


    viewModel {
        ProductDetailViewModel(
            savedStateHandle = get(),
            getProductByIdUseCase = get(),
            updateProductQuantityUseCase = get()
        )
    }

}