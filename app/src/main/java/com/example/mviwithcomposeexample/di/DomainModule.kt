package com.example.mviwithcomposeexample.di

import com.example.mviwithcomposeexample.domain.mapper.ProductToDomainMapper
import com.example.mviwithcomposeexample.domain.usecase.AddProductUseCase
import com.example.mviwithcomposeexample.domain.usecase.AddProductUseCaseImpl
import com.example.mviwithcomposeexample.domain.usecase.DeleteProductUseCase
import com.example.mviwithcomposeexample.domain.usecase.DeleteProductUseCaseImpl
import com.example.mviwithcomposeexample.domain.usecase.GetAllProductsUseCaseImpl
import com.example.mviwithcomposeexample.domain.usecase.GetAllProductsUseCase
import com.example.mviwithcomposeexample.domain.usecase.GetProductByIdUseCase
import com.example.mviwithcomposeexample.domain.usecase.GetProductByIdUseCaseImpl
import com.example.mviwithcomposeexample.domain.usecase.UpdateProductQuantityUseCase
import com.example.mviwithcomposeexample.domain.usecase.UpdateProductQuantityUseCaseImpl
import com.example.mviwithcomposeexample.domain.usecase.UpdateProductUseCase
import com.example.mviwithcomposeexample.domain.usecase.UpdateProductUseCaseImpl
import org.koin.dsl.module

val domainModule = module {

    factory {
        ProductToDomainMapper()
    }

    factory<GetAllProductsUseCase> {
        GetAllProductsUseCaseImpl(
            productRepository = get(),
            productToDomainMapper = get()
        )
    }

    factory<AddProductUseCase> {
        AddProductUseCaseImpl(
            productRepository = get()
        )
    }

    factory<DeleteProductUseCase> {
        DeleteProductUseCaseImpl(
            productRepository = get()
        )
    }

    factory<UpdateProductUseCase> {
        UpdateProductUseCaseImpl(
            productRepository = get()
        )
    }

    factory<GetProductByIdUseCase> {
        GetProductByIdUseCaseImpl(
            productRepository = get(),
            productToDomainMapper = get()
        )
    }

    factory<UpdateProductQuantityUseCase> {
        UpdateProductQuantityUseCaseImpl(
            productRepository = get()
        )
    }

}