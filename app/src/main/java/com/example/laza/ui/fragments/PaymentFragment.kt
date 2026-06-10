package com.example.laza.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.laza.adapters.CardAdapter
import com.example.laza.databinding.FragmentPaymentBinding
import com.example.laza.ui.viewmodels.PaymentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PaymentFragment : Fragment() {

    private var _binding: FragmentPaymentBinding? = null
    private val binding get() = _binding!!

    private val paymentViewModel: PaymentViewModel by viewModel()

    private lateinit var cardAdapter: CardAdapter
    val bottomSheet = AddCardFragment()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPaymentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPager()
        setupObserver()
        setupClickListeners()
    }

    private fun setupViewPager() {
        cardAdapter = CardAdapter { card ->
            // ✅ select card on tap
            paymentViewModel.cards.value?.let { allCards ->
                paymentViewModel.selectCard(card, allCards)
            }
        }
        binding.cardViewPager.adapter = cardAdapter

    }

    private fun setupObserver() {
        paymentViewModel.cards.observe(viewLifecycleOwner) { cards ->
            cardAdapter.updateData(cards)

            if (cards.isEmpty()) {
                binding.cardViewPager.visibility = View.GONE
                binding.cardOwnerTV2.visibility = View.GONE
                binding.cardOwnerTextInputLayout.visibility = View.GONE
                binding.cardNumberTextInputLayout.visibility = View.GONE
                binding.textLL.visibility = View.GONE
                binding.expNoTYL.visibility = View.GONE
                binding.cvvNoTYL.visibility = View.GONE
                binding.cardNumberTV2.visibility = View.GONE
                binding.expTV.visibility = View.GONE
                binding.dellBTN.visibility = View.GONE
                binding.emptyCardsTV.visibility = View.VISIBLE
            } else {
                binding.cardViewPager.visibility = View.VISIBLE
                binding.dellBTN.visibility = View.VISIBLE
                binding.emptyCardsTV.visibility = View.GONE
                binding.cardViewPager.registerOnPageChangeCallback(object :
                    ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        paymentViewModel.cards.value?.let { cards ->
                            if (cards.isNotEmpty()) {
                                val selectedCard = cards[position]
                                binding.cardOwnerTextInputLayout.editText?.setText(selectedCard.cardOwner)
                                binding.cardNumberTextInputLayout.editText?.setText(selectedCard.cardNumber)
                                binding.expNoTYL.editText?.setText(selectedCard.expiryDate)
                                binding.cvvNoTYL.editText?.setText(selectedCard.cvv)
                            }
                        }
                    }
                })
            }
        }
    }

    private fun setupClickListeners() {
        binding.addCardBTN.setOnClickListener {
            // show add card dialog or navigate to add card fragment
            showAddCardDialog()
        }
        binding.dellBTN.setOnClickListener {
            val position = binding.cardViewPager.currentItem
            val cards = paymentViewModel.cards.value
            if (!cards.isNullOrEmpty()) {
                paymentViewModel.deleteCard(cards[position])
            }
        }
    }

    private fun showAddCardDialog() {
        bottomSheet.show(parentFragmentManager, bottomSheet.tag)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}








