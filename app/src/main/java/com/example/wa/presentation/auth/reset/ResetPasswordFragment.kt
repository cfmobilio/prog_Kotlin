package com.example.wa.presentation.auth.reset

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.wa.data.repository.AuthRepository
import com.example.wa.R
import com.example.wa.di.ResetPasswordViewModelFactory

class ResetPasswordFragment : Fragment() {

    private lateinit var viewModel: ResetPasswordViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.reset_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val authRepository = AuthRepository()
        val factory = ResetPasswordViewModelFactory(authRepository)
        viewModel = ViewModelProvider(this, factory)[ResetPasswordViewModel::class.java]

        setupUI(view)
        observeViewModel()
    }

    private fun setupUI(view: View) {
        val emailEditText = view.findViewById<EditText>(R.id.emailResetEditText)
        val resetButton = view.findViewById<Button>(R.id.sendResetButton)

        resetButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            viewModel.sendPasswordResetEmail(email)
        }
    }

    private fun observeViewModel() {
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            if (state.isLoading) {
                // Mostra loading
            }

            state.successMessage?.let { message ->
                Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
                viewModel.clearMessages()
            }

            state.errorMessage?.let { message ->
                Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
                viewModel.clearMessages()
            }
        }
    }
}

