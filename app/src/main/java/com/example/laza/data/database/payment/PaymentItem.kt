package com.example.laza.data.database.payment

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "payment")
data class PaymentCard(
    @PrimaryKey(autoGenerate = true)  // ✅ auto ID — user can have multiple cards
    val id: Int = 0,
    val cardNumber: String,           // "1234 5678 9012 3456"
    val cardOwner: String,            // "John Doe"
    val expiryDate: String,           // "12/26"
    val cvv: String,                  // "123"
    val isSelected: Boolean = false   // ✅ which card to pay with
)