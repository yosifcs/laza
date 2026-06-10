package com.example.laza.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.laza.data.database.payment.PaymentCard
import com.example.laza.data.repos.PaymentRepository
import kotlinx.coroutines.launch

class PaymentViewModel(private val paymentRepository: PaymentRepository) : ViewModel() {

    val cards: LiveData<List<PaymentCard>> = paymentRepository.cards

    fun addCard(cardItem: PaymentCard) {
        viewModelScope.launch {
            paymentRepository.insertCard(cardItem)  // ✅
        }
    }

    fun deleteCard(card: PaymentCard) {
        viewModelScope.launch {
            paymentRepository.deleteCard(card)
        }
    }

    fun selectCard(card: PaymentCard, allCards: List<PaymentCard>) {
        viewModelScope.launch {
            // deselect all first
            allCards.forEach {
                if (it.isSelected) paymentRepository.updateCard(it.copy(isSelected = false))
            }
            // select chosen card
            paymentRepository.updateCard(card.copy(isSelected = true))
        }
    }
}