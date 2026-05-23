package com.example.laza.data.repos

import com.example.laza.data.api.RetrofitInstance
import com.example.laza.data.models.productsModels.ProductsResponse
import retrofit2.HttpException
import retrofit2.Response

class ProductsRepository {
    private val productsService = RetrofitInstance.apiService

    suspend fun getProducts(): Response<ProductsResponse> {

        val response = productsService.getProducts()
        if (response.isSuccessful) {
            return response
        } else {
            throw HttpException(response)
        }
    }
}