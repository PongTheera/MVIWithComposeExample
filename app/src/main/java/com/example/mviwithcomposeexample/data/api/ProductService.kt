package com.example.mviwithcomposeexample.data.api

import com.example.mviwithcomposeexample.data.model.ProductRequestModel
import com.example.mviwithcomposeexample.data.model.ProductResponseModel
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ProductService {
    @GET("products")
    suspend fun getProducts(): List<ProductResponseModel>

    @POST("products")
    suspend fun addProduct(@Body product: ProductRequestModel)

    @DELETE("products/{id}")
    suspend fun deleteProduct(@Path("id") id: Int)

    @PUT("products")
    suspend fun updateProduct(@Body id: Int, @Body product: ProductRequestModel)

    @GET("products/{id}")
    suspend fun getProductById(@Path("id") id: Int): ProductResponseModel?

    @POST("updateProductTotal")
    suspend fun updateProductTotal(@Path("id") id: Int, total: Int)
}