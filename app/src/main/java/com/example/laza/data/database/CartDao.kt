package com.example.laza.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface CartDao {

    // get all cart items — returns LiveData so UI updates automatically
    @Query("SELECT * FROM cart")
    fun getCartItems(): LiveData<List<CartItem>>

    // add item — if same productId+size exists, replace it
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToCart(cartItem: CartItem)

    // remove one item
    @Delete
    suspend fun removeFromCart(cartItem: CartItem)

    // update quantity
    @Update
    suspend fun updateCartItem(cartItem: CartItem)

    // clear entire cart
    @Query("DELETE FROM cart")
    suspend fun clearCart()

    // check if product already in cart
    @Query("SELECT * FROM cart WHERE productId = :productId AND selectedSize = :size LIMIT 1")
    suspend fun getCartItem(productId: String, size: String): CartItem?
}