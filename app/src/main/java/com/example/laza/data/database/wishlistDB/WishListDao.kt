package com.example.laza.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.laza.data.database.wishlistDB.WishlistItem

@Dao
interface WishlistDao {

    @Query("SELECT * FROM wishlist")
    fun getWishlistItems(): LiveData<List<WishlistItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToWishlist(wishlistItem: WishlistItem)

    @Delete
    suspend fun removeFromWishlist(wishlistItem: WishlistItem)

    @Query("DELETE FROM wishlist")
    suspend fun clearWishlist()

    @Query("SELECT * FROM wishlist WHERE productId = :productId LIMIT 1")
    suspend fun getWishlistItem(productId: String): WishlistItem?
}