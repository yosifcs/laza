package com.example.laza.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.laza.R
import com.example.laza.data.database.CartItem

class CartAdapter(
    private val onIncrease: (CartItem) -> Unit,
    private val onDecrease: (CartItem) -> Unit,
    private val onDelete: (CartItem) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {


    private val cartDifferCallback = object : DiffUtil.ItemCallback<CartItem>() {
        override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem.productId == newItem.productId && oldItem.selectedSize == newItem.selectedSize
        }

        override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, cartDifferCallback)

    fun updateData(cartItem: List<CartItem>) {
        differ.submitList(cartItem)
    }

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cover: ImageView = itemView.findViewById(R.id.productCoverIV)
        val productName: TextView = itemView.findViewById(R.id.productNameTV)
        val productPrice: TextView = itemView.findViewById(R.id.productPriceTV)
        val productSize: TextView = itemView.findViewById(R.id.sizeChip)
        val productQuantity: TextView = itemView.findViewById(R.id.amountTV)
        val incBTN: Button = itemView.findViewById(R.id.incBTN)  // ✅
        val decBTN: Button = itemView.findViewById(R.id.decBTN)  // ✅
        val delBTN: Button = itemView.findViewById(R.id.detBTN)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CartViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: CartViewHolder,
        position: Int
    ) {
        val cartItem = differ.currentList[position]
        Glide.with(holder.itemView.context)
            .load(cartItem.imageCover)
            .into(holder.cover)
        holder.productName.text = cartItem.title
        holder.productPrice.text = "${cartItem.price} EGP"
        holder.productSize.text = cartItem.selectedSize
        holder.productQuantity.text = cartItem.quantity.toString()

        holder.incBTN.setOnClickListener {
            onIncrease(cartItem)
        }
        holder.decBTN.setOnClickListener {

            onDecrease(cartItem)
        }
        holder.delBTN.setOnClickListener {
            onDelete(cartItem)
        }

    }

    override fun getItemCount(): Int = differ.currentList.size
}