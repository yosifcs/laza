package com.example.laza.data.api

import com.example.laza.data.models.categoriesModels.CategoriesResponse
import com.example.laza.data.models.productDetailsModels.ProductDetailsResponse
import com.example.laza.data.models.productsModels.ProductsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("api/v1/categories")
    suspend fun getCategories(): Response<CategoriesResponse>

    @GET("api/v1/products")
    suspend fun getProducts(): Response<ProductsResponse>

    @GET("api/v1/products")
    suspend fun searchProducts(
        @Query("keyword") keyword: String
    ): Response<ProductsResponse>
    
    @GET("api/v1/products/{id}")
    suspend fun getProductsById(@Path("id") id: String): Response<ProductDetailsResponse>
}