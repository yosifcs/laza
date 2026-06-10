package com.example.laza.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.laza.data.database.cartDB.CartDao
import com.example.laza.data.database.cartDB.CartItem
import com.example.laza.data.database.payment.PaymentCard
import com.example.laza.data.database.payment.PaymentDao
import com.example.laza.data.database.wishlistDB.WishlistItem
import com.example.laza.data.local.WishlistDao

@Database(
    entities = [CartItem::class, WishlistItem::class, PaymentCard::class],
    version = 3,
    exportSchema = false
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao
    abstract fun wishlistDao(): WishlistDao
    abstract fun paymentDao(): PaymentDao
}