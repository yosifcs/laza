package com.example.laza.data.repos

import com.example.laza.data.database.payment.PaymentCard
import com.example.laza.data.database.payment.PaymentDao

class PaymentRepository(private val paymentDao: PaymentDao) {
    val cards = paymentDao.getCards()
    suspend fun insertCard(card: PaymentCard) = paymentDao.insertCard(card)
    suspend fun updateCard(card: PaymentCard) = paymentDao.updateCard(card)
    suspend fun deleteCard(card: PaymentCard) = paymentDao.deleteCard(card)
    suspend fun getSelectedCard() = paymentDao.getSelectedCard()
}