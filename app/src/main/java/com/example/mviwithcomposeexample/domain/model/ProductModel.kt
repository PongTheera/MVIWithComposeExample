package com.example.mviwithcomposeexample.domain.model

data class ProductModel(
    val id: Int,
    val name: String,
    val price: Double,
    val description: String,
    val formattedName: String,
    val formattedPrice: String,
    val formattedDescription: String,
    val total: Int
)