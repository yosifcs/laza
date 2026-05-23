package com.example.laza.data.models.productsModels

data class ProductsResponse(
    val `data`: List<Product>,
    val metadata: Metadata,
    val results: Int
)