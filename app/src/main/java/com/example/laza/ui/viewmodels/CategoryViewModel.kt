package com.example.laza.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.laza.data.models.categoriesModels.CategoriesResponse
import com.example.laza.data.repos.CategoryRepository
import com.example.laza.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class CategoryViewModel(
    private val repo: CategoryRepository
) : ViewModel() {
    private val _categories = MutableLiveData<Resource<CategoriesResponse>>()
    val categories: LiveData<Resource<CategoriesResponse>> = _categories

    fun fetchCategories() {
        viewModelScope.launch {
            _categories.postValue(Resource.Loading())
            try {
                val response = repo.getCategories()
                response.body()?.let {
                    _categories.postValue(Resource.Success(it))
                } ?: _categories.postValue(Resource.Error("Empty response body"))
            } catch (e: HttpException) {
                _categories.postValue(Resource.Error("HTTP Error: ${e.code()}"))
            } catch (e: IOException) {
                _categories.postValue(Resource.Error("Network Error: ${e.message}"))
            }
        }
    }
}