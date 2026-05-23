package com.example.laza.data.api

import com.example.laza.data.models.categoriesModels.CategoriesResponse
import com.example.laza.data.models.productsModels.ProductsResponse
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET("api/v1/categories")
    suspend fun getCategories(): Response<CategoriesResponse>

    @GET("api/v1/products")
    suspend fun getProducts(): Response<ProductsResponse>

}