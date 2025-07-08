package com.example.wa.presentation.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.wa.R

class AccessFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.access_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val registra = view.findViewById<Button>(R.id.registerButton)
        val login = view.findViewById<Button>(R.id.loginButton)

        registra.setOnClickListener {
            findNavController().navigate(R.id.action_accessoFragment_to_registerFragment)
        }

        login.setOnClickListener {
            findNavController().navigate(R.id.action_accessoFragment_to_loginFragment)
        }

    }
}