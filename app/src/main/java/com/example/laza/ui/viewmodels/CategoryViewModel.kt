package com.example.laza.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.laza.data.models.categoriesModels.CategoriesResponse
import com.example.laza.data.repos.CategoryRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class CategoryViewModel : ViewModel() {

    private val repo = CategoryRepository()

    private val _categories = MutableLiveData<CategoriesResponse>()
    val categories: LiveData<CategoriesResponse> = _categories

    //dol zy elstate mangement
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun fetchCategories() {
        viewModelScope.launch {
            _isLoading.postValue(true)
            try {
                val response = repo.getCategories()
                _categories.postValue(response.body())
            } catch (e: HttpException) {
                _error.postValue("HTTP Error: ${e.code()}")
            } catch (e: IOException) {
                _error.postValue("Network Error: Check your connection${e.message}")
            } finally {
                _isLoading.postValue(false)  // always runs — success or failure
            }
        }
    }
}