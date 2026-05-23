package com.example.laza.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.laza.R
import com.example.laza.data.models.productsModels.Product

class ProductsAdapter(
    private val onItemClick: (Product) -> Unit   // ✅ removed unused products param
) : RecyclerView.Adapter<ProductsAdapter.ProductViewHolder>() {

    private val productDifferCallback = object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, productDifferCallback)  // ✅ private

    fun updateData(newProducts: List<Product>) {   // ✅ clean public API
        differ.submitList(newProducts)
    }

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.productImage)
        val title: TextView = itemView.findViewById(R.id.productName)
        val price: TextView = itemView.findViewById(R.id.productPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = differ.currentList[position]
        holder.title.text = product.title
        holder.price.text = "${product.price} EGP"
        Glide.with(holder.itemView.context)
            .load(product.imageCover)
            .placeholder(R.drawable.boarding_man)
            .into(holder.image)
        holder.itemView.setOnClickListener { onItemClick(product) }
    }

    override fun getItemCount(): Int = differ.currentList.size
}