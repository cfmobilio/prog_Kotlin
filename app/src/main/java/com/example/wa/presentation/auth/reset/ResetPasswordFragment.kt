package com.example.wa.presentation.auth.reset

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.wa.R
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.wa.presentation.auth.reset.ResetPasswordViewModel.ResetState
import kotlinx.coroutines.flow.collectLatest

class ResetPasswordFragment : Fragment() {

    private val viewModel: ResetPasswordViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.reset_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val emailEditText = view.findViewById<EditText>(R.id.emailResetEditText)
        val resetButton = view.findViewById<Button>(R.id.sendResetButton)

        resetButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            if (email.isBlank()) {
                Toast.makeText(requireContext(), "Inserisci un'email valida", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.sendResetEmail(email)
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.resetState.collectLatest { state ->
                when (state) {
                    is ResetState.Success -> {
                        Toast.makeText(requireContext(), "Email inviata! Controlla la tua casella.", Toast.LENGTH_LONG).show()
                    }
                    is ResetState.Error -> {
                        Toast.makeText(requireContext(), "Errore: ${state.message}", Toast.LENGTH_LONG).show()
                    }
                    else -> {}
                }
            }
        }
    }
}

