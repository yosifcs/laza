package com.example.laza.data.models.productDetailsModels

data class Data(
    val __v: Int,
    val _id: String,
    val brand: Brand,
    val category: Category,
    val createdAt: String,
    val description: String,
    val id: String,
    val imageCover: String,
    val images: List<String>,
    val price: Int,
    val quantity: Int,
    val ratingsAverage: Double,
    val ratingsQuantity: Int,
    val reviews: List<Review>,
    val slug: String,
    val sold: Int,
    val subcategory: List<Subcategory>,
    val title: String,
    val updatedAt: String
)