package com.example.mviwithcomposeexample.di

import com.example.mviwithcomposeexample.data.api.MockProductService
import com.example.mviwithcomposeexample.data.api.ProductService
import com.example.mviwithcomposeexample.data.datasource.MockProductDataSource
import com.example.mviwithcomposeexample.data.datasource.MockProductDataSourceImpl
import com.example.mviwithcomposeexample.data.repository.MockProductRepository
import com.example.mviwithcomposeexample.data.repository.ProductRepository
import org.koin.dsl.module

val dataModule = module {

    single<ProductService> {
        MockProductService()
    }

    single<MockProductDataSource> {
        MockProductDataSourceImpl(
            productService = get()
        )
    }

    single<ProductRepository> {
        MockProductRepository(
            dataSource = get()
        )
    }

}