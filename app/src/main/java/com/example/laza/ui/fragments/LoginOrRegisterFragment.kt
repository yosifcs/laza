package com.example.laza.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.laza.R

class LoginOrRegisterFragment : Fragment(R.layout.fragment_login_or_register) {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_login_or_register, container, false)
        view.findViewById<Button>(R.id.buttonTwitter).setOnClickListener {}
        view.findViewById<Button>(R.id.buttonFacebook).setOnClickListener {}
        view.findViewById<Button>(R.id.buttonGoogle).setOnClickListener {}
        view.findViewById<Button>(R.id.bottomSignInTxtBtn).setOnClickListener {
            view.findNavController().navigate(R.id.action_loginOrRegisterFragment_to_loginFragment)
        }
        view.findViewById<Button>(R.id.createAccountBtn).setOnClickListener {
            view.findNavController()
                .navigate(R.id.action_loginOrRegisterFragment_to_signUp)
        }
        return view
    }
}