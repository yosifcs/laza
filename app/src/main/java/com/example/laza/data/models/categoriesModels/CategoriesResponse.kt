package com.example.laza.data.models.categoriesModels

data class CategoriesResponse(
    val `data`: List<Category>,
    val metadata: Metadata,
    val results: Int
)