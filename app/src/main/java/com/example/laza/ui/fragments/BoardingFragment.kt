package com.example.laza.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.laza.R

class BoardingFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_boarding, container, false)
        view.findViewById<Button>(R.id.menButton).setOnClickListener {
            view.findNavController()
                .navigate(R.id.action_boardingFragment_to_loginOrRegisterFragment)
        }
        view.findViewById<Button>(R.id.womenButton).setOnClickListener {
            view.findNavController()
                .navigate(R.id.action_boardingFragment_to_loginOrRegisterFragment)

        }

        return view
    }


}
