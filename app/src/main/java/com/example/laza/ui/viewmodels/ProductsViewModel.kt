package com.example.laza.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.laza.data.models.productsModels.Metadata
import com.example.laza.data.models.productsModels.Product
import com.example.laza.data.models.productsModels.ProductsResponse
import com.example.laza.data.repos.ProductsRepository
import com.example.laza.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class ProductsViewModel(

    private val productsRepository: ProductsRepository
) : ViewModel() {

    private val _products = MutableLiveData<Resource<ProductsResponse>>()
    val products: LiveData<Resource<ProductsResponse>> = _products

    private var selectedCategory: String = ""

    // keep original list to restore when search is cleared
    private var allProducts: List<Product> = emptyList()

    fun fetchProducts() {
        viewModelScope.launch {
            _products.postValue(Resource.Loading())
            try {
                val response = productsRepository.getProducts()
                response.body()?.let {
                    allProducts = it.data  // ✅ save here first
                    _products.postValue(Resource.Success(it))
                } ?: _products.postValue(Resource.Error("Empty response body"))
            } catch (e: HttpException) {
                _products.postValue(Resource.Error("HTTP Error: ${e.code()}"))
            } catch (e: IOException) {
                _products.postValue(Resource.Error("Network Error: ${e.message}"))
            }
        }
    }

    fun searchProducts(keyword: String) {
        if (keyword.isEmpty()) {
            // restore full list
            _products.postValue(
                Resource.Success(
                    ProductsResponse(
                        data = allProducts,
                        metadata = Metadata(1, 40, 1, 1),
                        results = allProducts.size
                    )
                )
            )
            return
        }

        val filtered = allProducts.filter { product ->
            product.title.contains(keyword, ignoreCase = true) ||
                    product.brand.name.contains(keyword, ignoreCase = true) ||
                    product.category.name.contains(keyword, ignoreCase = true)
        }

        _products.postValue(
            Resource.Success(
                ProductsResponse(
                    data = filtered,
                    metadata = Metadata(1, 40, 1, 1),
                    results = filtered.size
                )
            )
        )
    }

    fun filterByCategory(categoryName: String) {
        // if same category clicked again — deselect and show all
        if (selectedCategory == categoryName) {
            selectedCategory = ""
            _products.postValue(
                Resource.Success(
                    ProductsResponse(
                        data = allProducts,
                        metadata = Metadata(1, 40, 1, 1),
                        results = allProducts.size
                    )
                )
            )
            return
        }

        selectedCategory = categoryName

        val filtered = allProducts.filter { product ->
            product.category.name == categoryName
        }

        _products.postValue(
            Resource.Success(
                ProductsResponse(
                    data = filtered,
                    metadata = Metadata(1, 40, 1, 1),
                    results = filtered.size
                )
            )
        )
    }

}