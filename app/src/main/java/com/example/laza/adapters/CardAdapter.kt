package com.example.laza.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.laza.R
import com.example.laza.data.database.payment.PaymentCard

class CardAdapter(private val onCardSelected: (PaymentCard) -> Unit) :
    RecyclerView.Adapter<CardAdapter.CardViewHolder>() {
    private var cards: List<PaymentCard> = emptyList()


    private val cardDifferCallback = object : DiffUtil.ItemCallback<PaymentCard>() {
        override fun areItemsTheSame(oldItem: PaymentCard, newItem: PaymentCard): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PaymentCard, newItem: PaymentCard): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, cardDifferCallback)


    fun updateData(newCards: List<PaymentCard>) {
        differ.submitList(newCards)
    }

    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardNumber: TextView = itemView.findViewById(R.id.cardNumberTV)
        val cardOwner: TextView = itemView.findViewById(R.id.cardOwnerTV)
        val expiryDate: TextView = itemView.findViewById(R.id.expiryDateTV)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_card, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val card = differ.currentList[position]

        // ✅ mask card number
        val lastFour = card.cardNumber.takeLast(4)
        holder.cardNumber.text = "**** **** **** $lastFour"
        holder.cardOwner.text = card.cardOwner
        holder.expiryDate.text = card.expiryDate
        holder.itemView.setOnClickListener { onCardSelected(card) }
    }

    override fun getItemCount(): Int = differ.currentList.size
}