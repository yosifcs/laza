package com.example.laza.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.laza.R
import com.example.laza.databinding.FragmentHomeBinding
import com.example.laza.ui.adapters.ProductsAdapter
import com.example.laza.ui.viewmodels.CategoryViewModel
import com.example.laza.ui.viewmodels.ProductsViewModel
import com.example.laza.utils.Resource
import com.google.android.material.chip.Chip
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val categoriesViewModel: CategoryViewModel by viewModel()
    private val productsViewModel: ProductsViewModel by viewModel()

    private lateinit var productsAdapter: ProductsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupObservers()
        setupSearch()
        productsViewModel.fetchProducts()
        categoriesViewModel.fetchCategories()
    }

    private fun setupSearch() {
        binding.searchBar.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val keyword = s.toString().trim()
                productsViewModel.searchProducts(keyword)
            }
        })
    }

    private fun setupRecyclerView() {

        productsAdapter = ProductsAdapter { product ->
            val bundle = Bundle().apply {
                putString("productId", product.id)
            }
            findNavController().navigate(
                R.id.action_homeFragment_to_productDetailsFragment,
                bundle
            )
        }

        binding.productsRecyclerView.apply {
            adapter = productsAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)  // 2 columns
        }


    }

    private fun setupObservers() {

        categoriesViewModel.categories.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.homeChips.removeAllViews()
                    val allChip = Chip(requireContext())
                    allChip.text = "All"
                    allChip.isCheckable = true
                    allChip.isChecked = true  // selected by default
                    allChip.setOnClickListener {
                        productsViewModel.filterByCategory("")
                    }
                    resource.data.data.forEach { category ->
                        val chip = Chip(requireContext())
                        chip.text = category.name
                        chip.setOnClickListener {
                            productsViewModel.filterByCategory(category.name)
                        }
                        binding.homeChips.addView(chip)
                    }
                }

                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), resource.message, Toast.LENGTH_LONG).show()
                }

            }
        }

        productsViewModel.products.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.productsRecyclerView.visibility = View.GONE
                }

                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.productsRecyclerView.visibility = View.VISIBLE
                    productsAdapter.updateData(resource.data.data)
                }

                is Resource.Error -> {

                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), resource.message, Toast.LENGTH_LONG).show()
                }
            }
        }

    }

    // CRITICAL — prevents memory leaks in Fragments
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}