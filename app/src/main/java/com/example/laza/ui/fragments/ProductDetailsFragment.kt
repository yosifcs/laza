package com.example.laza.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.laza.R
import com.example.laza.adapters.ProductDetailsAdapter
import com.example.laza.data.database.cartDB.CartItem
import com.example.laza.data.models.productDetailsModels.Data
import com.example.laza.data.models.productDetailsModels.Review
import com.example.laza.databinding.FragmentProductDetailsBinding
import com.example.laza.ui.viewmodels.CartViewModel
import com.example.laza.ui.viewmodels.ProductDetailsViewModel
import com.example.laza.utils.Resource
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

class ProductDetailsFragment : Fragment() {
    private var _binding: FragmentProductDetailsBinding? = null
    private val binding get() = _binding!!

    private val productDetailsViewModel: ProductDetailsViewModel by viewModel()
    private lateinit var productDetailsAdapter: ProductDetailsAdapter

    private val cartViewModel: CartViewModel by viewModel()
    private var currentProduct: Data? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    fun setupClickListeners() {
        binding.addCartBTN.setOnClickListener {
            val product = currentProduct ?: return@setOnClickListener

            val sizeCategories = listOf("Men's Fashion", "Women's Fashion")
            val requiresSize = product.category.name in sizeCategories

            val selectedSize = if (requiresSize) {
                // only check size for fashion products
                when (binding.sizeChipGroup.checkedChipId) {
                    R.id.sChipAndroid -> "S"
                    R.id.mChipAndroid -> "M"
                    R.id.lChipAndroid -> "L"
                    R.id.xLChipAndroid -> "XL"
                    R.id.xxLChipAndroid -> "XXL"
                    else -> {
                        Toast.makeText(requireContext(), "Please select a size", Toast.LENGTH_SHORT)
                            .show()
                        return@setOnClickListener
                    }
                }
            } else {
                // electronics, books etc — no size needed
                "N/A"
            }

            val cartItem = CartItem(
                productId = product.id,
                title = product.title,
                imageCover = product.imageCover,
                price = product.price,
                selectedSize = selectedSize,
                quantity = 1
            )

            cartViewModel.addToCart(cartItem)
            Toast.makeText(requireContext(), "${product.title} added to cart ✅", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun setupRecyclerView() {
        productDetailsAdapter = ProductDetailsAdapter()
        binding.productDetailsRV.apply {
            adapter = productDetailsAdapter
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,  // horizontal image slider
                false
            )
            isNestedScrollingEnabled = false
        }

    }

    private fun setupReview(reviews: List<Review>) {
        if (reviews.isEmpty()) {
            binding.reviewTV.text = "No Reviews Yet"
            binding.reviewCard.visibility = View.GONE
            return
        }

        val firstReview = reviews.first()
        binding.reviewCard.visibility = View.VISIBLE
        binding.ratingTV.text = "${firstReview.rating} ratings"
        binding.nameTV.text = firstReview.user.name
        binding.commentTV.text = firstReview.review
        binding.ratingBar.rating = firstReview.rating.toFloat()
        binding.dateTV.text = formatDate(firstReview.createdAt)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupClickListeners()
        val productId = arguments?.getString("productId")
        productId?.let { productDetailsViewModel.fetchProductById(it) }
        setupObservers()

    }

    private fun setupObservers() {
        productDetailsViewModel.products.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {

                    binding.productDeatilsPB.visibility = View.VISIBLE

                }

                is Resource.Success -> {
                    currentProduct = resource.data.data
                    val product = resource.data.data
                    setupReview(product.reviews)
                    // categories that have sizes
                    val sizeCategories = listOf("Men's Fashion", "Women's Fashion")
                    if (product.category.name in sizeCategories) {
                        binding.sizeTV.visibility = View.VISIBLE
                        binding.sizeChipGroup.visibility = View.VISIBLE
                    } else {
                        binding.sizeTV.visibility = View.GONE
                        binding.sizeChipGroup.visibility = View.GONE
                    }
                    binding.productDeatilsPB.visibility = View.GONE
                    binding.productDetailsRV.visibility = View.VISIBLE
                    productDetailsAdapter.updateData(product.images)
                    binding.titleTV.text = product.title
                    binding.brandTV.text = product.brand.name
                    binding.priceInNoTV.text = "${product.price} EGP"
                    binding.descTextTV.text = product.description
                    binding.totalPriceNoTV.text = "${product.price + 5} EGP"
                    Glide.with(this)
                        .load(product.imageCover)
                        .into(binding.coverIVDetails)
                }

                is Resource.Error -> {

                    binding.productDeatilsPB.visibility = View.GONE
                    Toast.makeText(requireContext(), resource.message, Toast.LENGTH_LONG).show()
                }
            }
        }

    }

    private fun formatDate(dateString: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            outputFormat.format(inputFormat.parse(dateString)!!)
        } catch (e: Exception) {
            dateString
        }
    }
}