package com.example.laza.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.laza.R

class ProductDetailsAdapter :
    RecyclerView.Adapter<ProductDetailsAdapter.ProductDetailsViewHolder>() {


    private val productDetailsDifferCallback = object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, productDetailsDifferCallback)


    fun updateData(images: List<String>) {   // ✅ clean public API
        differ.submitList(images)
    }

    inner class ProductDetailsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.productDetailsIV)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductDetailsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product_details, parent, false)
        return ProductDetailsViewHolder(view)
    }


    override fun onBindViewHolder(
        holder: ProductDetailsViewHolder,
        position: Int
    ) {
        val imageUrl = differ.currentList[position]
        Glide.with(holder.itemView.context)
            .load(imageUrl)
            .into(holder.image)

    }

    override fun getItemCount(): Int = differ.currentList.size
}