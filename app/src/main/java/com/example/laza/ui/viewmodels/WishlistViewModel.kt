package com.example.laza.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.laza.data.database.wishlistDB.WishlistItem
import com.example.laza.data.repos.WishlistRepository
import kotlinx.coroutines.launch

class WishlistViewModel(private val wishlistRepository: WishlistRepository) : ViewModel() {
    val wishlistItems: LiveData<List<WishlistItem>> = wishlistRepository.getWishlistItems()
    fun addToWishlist(wishlistItem: WishlistItem) {
        viewModelScope.launch {
            val existing = wishlistRepository.getWishlistItem(wishlistItem.productId)
            if (existing != null) {
                wishlistRepository.removeFromWishlist(wishlistItem)
            } else {
                wishlistRepository.addToWishlist(wishlistItem)
            }

        }
    }

    fun removeFromWishlist(wishlistItem: WishlistItem) {
        viewModelScope.launch {
            wishlistRepository.removeFromWishlist(wishlistItem)
        }
    }

    fun clearWishlist() {
        viewModelScope.launch {
            wishlistRepository.clearWishlist()
        }
    }
}