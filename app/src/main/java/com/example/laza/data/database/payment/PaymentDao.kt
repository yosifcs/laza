package com.example.laza.data.database.payment

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface PaymentDao {

    @Query("SELECT * FROM payment")
    fun getCards(): LiveData<List<PaymentCard>>

    @Insert
    suspend fun insertCard(card: PaymentCard)

    @Update
    suspend fun updateCard(card: PaymentCard)

    @Delete
    suspend fun deleteCard(card: PaymentCard)

    @Query("SELECT * FROM payment WHERE isSelected = 1 LIMIT 1")
    suspend fun getSelectedCard(): PaymentCard?
}


