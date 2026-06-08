package com.example.laza.data.repos

import androidx.lifecycle.LiveData
import com.example.laza.data.database.CartDao
import com.example.laza.data.database.CartItem

class CartRepository(private val cartDao: CartDao) {

    fun getCartItems(): LiveData<List<CartItem>> = cartDao.getCartItems()

    suspend fun addToCart(cartItem: CartItem) = cartDao.addToCart(cartItem)

    suspend fun removeFromCart(cartItem: CartItem) = cartDao.removeFromCart(cartItem)

    suspend fun updateCartItem(cartItem: CartItem) = cartDao.updateCartItem(cartItem)

    suspend fun clearCart() = cartDao.clearCart()

    suspend fun getCartItem(productId: String, size: String) =
        cartDao.getCartItem(productId, size)
}