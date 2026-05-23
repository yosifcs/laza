package com.example.laza.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.laza.databinding.FragmentHomeBinding
import com.example.laza.ui.adapters.ProductsAdapter
import com.example.laza.ui.viewmodels.CategoryViewModel
import com.example.laza.ui.viewmodels.ProductsViewModel
import com.google.android.material.chip.Chip

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val categoriesViewModel: CategoryViewModel by viewModels()
    private val productsViewModel: ProductsViewModel by viewModels()
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
        productsViewModel.fetchProducts()
        categoriesViewModel.fetchCategories()
    }

    private fun setupRecyclerView() {
        // create adapter with an empty list first
        productsAdapter = ProductsAdapter { product ->
            // handle item click
            Toast.makeText(requireContext(), product.title, Toast.LENGTH_SHORT).show()
        }

        binding.productsRecyclerView.apply {
            adapter = productsAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)  // 2 columns
        }
    }

    private fun setupObservers() {

        // loading state
        categoriesViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            print("loading")
        }
        productsViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            print("loading")
        }


        // success state
        categoriesViewModel.categories.observe(viewLifecycleOwner) { response ->
            binding.homeChips.removeAllViews()   // clear old chips first
            response.data.forEach { category ->
                val chip = Chip(requireContext())
                chip.text = category.name
                chip.setOnClickListener {
                    // handle chip click — navigate to category screen etc.
                }
                binding.homeChips.addView(chip)
            }
        }
        productsViewModel.products.observe(viewLifecycleOwner) { response ->
            productsAdapter.updateData(response.data)  // feed data to adapter
        }

        // error state
        categoriesViewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
        }
        productsViewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
        }
    }

    // CRITICAL — prevents memory leaks in Fragments
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}