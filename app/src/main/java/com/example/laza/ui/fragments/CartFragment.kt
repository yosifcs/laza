package com.example.laza.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.laza.R
import com.example.laza.adapters.CartAdapter
import com.example.laza.databinding.FragmentCartBinding
import com.example.laza.ui.viewmodels.CartViewModel
import com.example.laza.ui.viewmodels.PaymentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class CartFragment : Fragment() {
    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!

    private val cartViewModel: CartViewModel by viewModel()
    private val paymentViewModel: PaymentViewModel by viewModel()

    private lateinit var cartAdapter: CartAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupObserver()
        setupPaymentObserver()
    }

    private fun setupRecyclerView() {
        cartAdapter = CartAdapter(
            onIncrease = { cartItem ->
                cartViewModel.updateQuantity(
                    cartItem,
                    cartItem.quantity + 1
                )
            },
            onDecrease = { cartItem ->
                cartViewModel.updateQuantity(
                    cartItem,
                    cartItem.quantity - 1
                )
            },
            onDelete = { cartItem -> cartViewModel.removeFromCart(cartItem) }
        )
        binding.productCardCartRV.apply {
            adapter = cartAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupObserver() {
        cartViewModel.cartItems.observe(viewLifecycleOwner) { items ->
            cartAdapter.updateData(items)
            binding.shopNowBTN.setOnClickListener {
                findNavController().navigate(R.id.action_cartFragment_to_homeFragment)
            }
            if (items.isEmpty()) {
                binding.emptyStateLayout.visibility = View.VISIBLE
                binding.cartContentLayout.visibility = View.GONE
            } else {
                binding.emptyStateLayout.visibility = View.GONE
                binding.cartContentLayout.visibility = View.VISIBLE
                val subtotal = items.sumOf { it.price * it.quantity }
                val shipping = 10
                binding.subTotalNoTV.text = "$subtotal EGP"
                binding.shippingCostNoTV.text = "$shipping EGP"
                binding.totalPricenoTV.text = "${subtotal + shipping} EGP"
            }
        }
    }

    private fun setupPaymentObserver() {
        paymentViewModel.cards.observe(viewLifecycleOwner) { cards ->
            val selectedCard = cards.firstOrNull { it.isSelected } ?: cards.firstOrNull()

            if (selectedCard != null) {
                binding.selectedCardLayout.visibility = View.VISIBLE
                binding.noCardLayout.visibility = View.GONE
                binding.selectedCardNumberTV.text = selectedCard.cardNumber
                binding.selectedCardOwnerTV.text = selectedCard.cardOwner
            } else {
                binding.selectedCardLayout.visibility = View.GONE
                binding.noCardLayout.visibility = View.VISIBLE
                binding.goToWalletTV.setOnClickListener {
                    findNavController().navigate(R.id.action_cartFragment_to_paymentFragment)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}