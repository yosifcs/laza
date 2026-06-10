package com.example.laza.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.laza.data.database.cartDB.CartItem
import com.example.laza.data.repos.CartRepository
import kotlinx.coroutines.launch

class CartViewModel(private val cartRepository: CartRepository) : ViewModel() {

    // LiveData — auto updates UI when cart changes
    val cartItems: LiveData<List<CartItem>> = cartRepository.getCartItems()

    fun addToCart(cartItem: CartItem) {
        viewModelScope.launch {
            val existing = cartRepository.getCartItem(cartItem.productId, cartItem.selectedSize)
            if (existing != null) {
                // ✅ create a new copy with updated quantity
                val updated = existing.copy(quantity = existing.quantity + 1)
                cartRepository.updateCartItem(updated)
            } else {
                cartRepository.addToCart(cartItem)
            }
        }
    }

    fun removeFromCart(cartItem: CartItem) {
        viewModelScope.launch {
            cartRepository.removeFromCart(cartItem)
        }
    }

    fun updateQuantity(cartItem: CartItem, newQuantity: Int) {
        viewModelScope.launch {
            if (newQuantity <= 0) {
                cartRepository.removeFromCart(cartItem)
            } else {
                // ✅ create a new copy
                val updated = cartItem.copy(quantity = newQuantity)
                cartRepository.updateCartItem(updated)
            }
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            cartRepository.clearCart()
        }
    }
}