package com.example.laza.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.laza.data.models.productDetailsModels.ProductDetailsResponse
import com.example.laza.data.repos.ProductsRepository
import com.example.laza.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class ProductDetailsViewModel(
    private val productsRepository: ProductsRepository
) : ViewModel() {
    private val _products = MutableLiveData<Resource<ProductDetailsResponse>>()
    val products: LiveData<Resource<ProductDetailsResponse>> = _products

    fun fetchProductById(id: String) {
        viewModelScope.launch {
            _products.postValue(Resource.Loading())
            try {
                val response = productsRepository.getProductsById(id)
                response.body()?.let {
                    _products.postValue(Resource.Success(it))
                } ?: _products.postValue(Resource.Error("Empty response body"))
            } catch (e: HttpException) {
                _products.postValue(Resource.Error("HTTP Error: ${e.code()}"))
            } catch (e: IOException) {
                _products.postValue(Resource.Error("Network Error: ${e.message}"))
            }
        }
    }
}