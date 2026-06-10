package com.example.laza.data.database.wishlistDB

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wishlist")
data class WishlistItem(
    @PrimaryKey
    val productId: String,
    val title: String,
    val imageCover: String,
    val price: Int,
)