package com.example.laza.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.laza.data.database.payment.PaymentCard
import com.example.laza.databinding.FragmentAddCardBinding
import com.example.laza.ui.viewmodels.PaymentViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel


class AddCardFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentAddCardBinding? = null
    private val binding get() = _binding!!

    private val paymentViewModel: PaymentViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddCardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCardNumberWatcher()
        setupExpiryWatcher()
        binding.saveCardBTN.setOnClickListener {
            saveCardToDatabase()
        }
    }

    private fun setupCardNumberWatcher() {
        binding.cardNumberET.addTextChangedListener(object : TextWatcher {
            private var isFormatting = false

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (isFormatting || s == null) return
                isFormatting = true

                val digits = s.filter { it.isDigit() }.take(16)
                val formatted = digits.chunked(4).joinToString(" ")

                s.replace(0, s.length, formatted)
                binding.cardNumberET.setSelection(formatted.length)

                isFormatting = false
            }
        })
    }

    private fun setupExpiryWatcher() {
        binding.expET.addTextChangedListener(object : TextWatcher {
            private var isFormatting = false
            private var previousLength = 0

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                previousLength = s?.length ?: 0
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (isFormatting || s == null) return
                isFormatting = true

                val digits = s.toString().replace("/", "")
                // Don't auto-insert slash if user is deleting
                val formatted = if (digits.length >= 2 && s.length >= previousLength) {
                    "${digits.take(2)}/${digits.drop(2).take(2)}"
                } else {
                    digits
                }

                s.replace(0, s.length, formatted)
                binding.expET.setSelection(formatted.length)

                isFormatting = false
            }
        })
    }

    private fun saveCardToDatabase() {
        val owner = binding.cardOwnerET.text.toString().trim()
        val number = binding.cardNumberET.text.toString().trim()
        val expiry = binding.expET.text.toString().trim()
        val cvv = binding.cvvET.text.toString().trim()

        if (owner.isEmpty() || number.isEmpty() || expiry.isEmpty() || cvv.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Validate card number has exactly 16 digits
        if (number.replace(" ", "").length != 16) {
            Toast.makeText(requireContext(), "Card number must be 16 digits", Toast.LENGTH_SHORT)
                .show()
            return
        }

        // Validate expiry format MM/YY
        if (!expiry.matches(Regex("\\d{2}/\\d{2}"))) {
            Toast.makeText(requireContext(), "Invalid expiry date", Toast.LENGTH_SHORT).show()
            return
        }

        // Validate CVV is exactly 3 digits
        if (cvv.length != 3) {
            Toast.makeText(requireContext(), "CVV must be 3 digits", Toast.LENGTH_SHORT).show()
            return
        }

        val newCard = PaymentCard(
            cardOwner = owner,
            cardNumber = number,
            expiryDate = expiry,
            cvv = cvv,
            isSelected = false
        )

        paymentViewModel.addCard(newCard)
        Toast.makeText(requireContext(), "Card saved successfully!", Toast.LENGTH_SHORT).show()
        dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}