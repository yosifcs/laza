package com.example.laza.data.repos

import androidx.lifecycle.LiveData
import com.example.laza.data.database.wishlistDB.WishlistItem
import com.example.laza.data.local.WishlistDao

class WishlistRepository(private val wishlistDao: WishlistDao) {
    fun getWishlistItems(): LiveData<List<WishlistItem>> = wishlistDao.getWishlistItems()
    suspend fun addToWishlist(wishlistItem: WishlistItem) = wishlistDao.addToWishlist(wishlistItem)
    suspend fun removeFromWishlist(wishlistItem: WishlistItem) =
        wishlistDao.removeFromWishlist(wishlistItem)

    suspend fun clearWishlist() = wishlistDao.clearWishlist()
    suspend fun getWishlistItem(productId: String) = wishlistDao.getWishlistItem(productId)

}
