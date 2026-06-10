package com.example.laza.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.laza.R
import com.example.laza.adapters.WishlistAdapter
import com.example.laza.databinding.FragmentWishlistBinding
import com.example.laza.ui.viewmodels.WishlistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class WishlistFragment : Fragment() {
    private var _binding: FragmentWishlistBinding? = null
    private val binding get() = _binding!!

    private val wishlistViewModel: WishlistViewModel by viewModel()
    private lateinit var wishlistAdapter: WishlistAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWishlistBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupObservers()
    }

    private fun setupRecyclerView() {

        wishlistAdapter = WishlistAdapter(
            onItemClick = { product ->
                val bundle = Bundle().apply { putString("productId", product.productId) }
                findNavController().navigate(
                    R.id.action_wishlistFragment_to_productDetailsFragment,
                    bundle
                )
            },
            onDelete = { wishlistItem ->
                wishlistViewModel.removeFromWishlist(wishlistItem)  // ✅
            }
        )
        binding.wishListRecyclerView.apply {
            adapter = wishlistAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)  // 2 columns
        }
    }

    private fun setupObservers() {

        wishlistViewModel.wishlistItems.observe(viewLifecycleOwner) { items ->
            wishlistAdapter.updateData(items)
            if (items.isEmpty()) {
                binding.wishListRecyclerView.visibility = View.GONE
                binding.emptyWishList.visibility = View.VISIBLE
                binding.wishlistSize.visibility = View.GONE
            } else {
                binding.emptyWishList.visibility = View.GONE
                binding.wishListRecyclerView.visibility = View.VISIBLE
                binding.wishlistSize.text = "${items.size} items"
            }
            

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
