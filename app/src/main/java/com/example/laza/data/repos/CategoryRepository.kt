package com.example.laza.data.repos

import com.example.laza.data.api.RetrofitInstance
import com.example.laza.data.models.categoriesModels.CategoriesResponse

import retrofit2.HttpException
import retrofit2.Response

class CategoryRepository {

    private val categoryService = RetrofitInstance.apiService

    suspend fun getCategories(): Response<CategoriesResponse> {
        val response = categoryService.getCategories()
        if (response.isSuccessful) {
            return response
        } else {
            throw HttpException(response)
        }
    }
}