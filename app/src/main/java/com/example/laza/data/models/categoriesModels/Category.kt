package com.example.laza.data.models.categoriesModels

import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("_id") val id: String,
    val createdAt: String,
    val image: String,
    val name: String,
    val slug: String,
    val updatedAt: String
)