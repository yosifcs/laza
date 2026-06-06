package com.example.laza.data.repos

import com.example.laza.data.api.ApiService
import com.example.laza.data.models.productDetailsModels.ProductDetailsResponse
import com.example.laza.data.models.productsModels.ProductsResponse
import retrofit2.HttpException
import retrofit2.Response

class ProductsRepository(private val productsService: ApiService) {

    suspend fun getProducts(): Response<ProductsResponse> {

        val response = productsService.getProducts()
        if (response.isSuccessful)
            return response
        else
            throw HttpException(response)

    }

    suspend fun getProductsById(id: String): Response<ProductDetailsResponse> {
        val response = productsService.getProductsById(id)
        if (response.isSuccessful)
            return response
        else
            throw HttpException(response)

    }

    suspend fun searchProducts(keyword: String): Response<ProductsResponse> {
        val response = productsService.searchProducts(keyword)
        return if (response.isSuccessful) response
        else throw HttpException(response)
    }
}