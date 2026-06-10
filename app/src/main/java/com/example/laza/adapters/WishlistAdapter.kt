package com.example.laza.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.laza.R
import com.example.laza.data.database.wishlistDB.WishlistItem

class WishlistAdapter(
    private val onItemClick: (WishlistItem) -> Unit,
    private val onDelete: (WishlistItem) -> Unit
) : RecyclerView.Adapter<WishlistAdapter.WishlistViewHolder>() {
    private val wishlistDifferCallback = object : DiffUtil.ItemCallback<WishlistItem>() {
        override fun areItemsTheSame(oldItem: WishlistItem, newItem: WishlistItem): Boolean {
            return oldItem.productId == newItem.productId
        }

        override fun areContentsTheSame(oldItem: WishlistItem, newItem: WishlistItem): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, wishlistDifferCallback)

    fun updateData(wishlistItem: List<WishlistItem>) {
        differ.submitList(wishlistItem)
    }

    inner class WishlistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.productImage)
        val title: TextView = itemView.findViewById(R.id.productName)
        val price: TextView = itemView.findViewById(R.id.productPrice)
        val fav: ImageButton = itemView.findViewById(R.id.favBTN)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WishlistViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return WishlistViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: WishlistViewHolder,
        position: Int
    ) {
        val wishlistItem = differ.currentList[position]
        holder.title.text = wishlistItem.title
        holder.itemView.setOnClickListener { onItemClick(wishlistItem) }
        holder.price.text = "${wishlistItem.price} EGP"
        Glide.with(holder.itemView.context)
            .load(wishlistItem.imageCover)
            .placeholder(R.drawable.boarding_man)
            .into(holder.image)
        holder.fav.setOnClickListener { onDelete(wishlistItem) }

    }

    override fun getItemCount(): Int = differ.currentList.size
}