package com.example.laza.data.models.productDetailsModels

data class Review(
    val __v: Int,
    val _id: String,
    val createdAt: String,
    val product: String,
    val rating: Int,
    val review: String,
    val updatedAt: String,
    val user: User
)