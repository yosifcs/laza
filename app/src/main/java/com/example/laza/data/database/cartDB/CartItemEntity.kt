package com.example.laza.data.database.cartDB

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart")
data class CartItem(
    @PrimaryKey
    val productId: String,      // product _id from API
    val title: String,
    val imageCover: String,
    val price: Int,
    val selectedSize: String,
    val quantity: Int = 1
)