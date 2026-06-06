package com.example.laza.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.laza.adapters.ProductDetailsAdapter
import com.example.laza.data.models.productDetailsModels.Review
import com.example.laza.databinding.FragmentProductDetailsBinding
import com.example.laza.ui.viewmodels.ProductDetailsViewModel
import com.example.laza.utils.Resource
import com.google.android.material.chip.Chip
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

class ProductDetailsFragment : Fragment() {
    private var _binding: FragmentProductDetailsBinding? = null
    private val binding get() = _binding!!

    private val productDetailsViewModel: ProductDetailsViewModel by viewModel()
    private lateinit var productDetailsAdapter: ProductDetailsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    fun setupClickListeners() {
        binding.sizeChipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            val selectedChip = group.findViewById<Chip>(
                checkedIds.firstOrNull() ?: return@setOnCheckedStateChangeListener
            )
            val selectedSize = selectedChip.text.toString()
            // use selectedSize
        }
        binding.addCartBTN.setOnClickListener { }
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