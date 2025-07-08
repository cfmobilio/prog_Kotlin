package com.example.wa.presentation.auth.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.wa.data.repository.AuthRepository
import com.example.wa.R
import com.example.wa.di.LoginViewModelFactory
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.SignInButton

class LoginFragment : Fragment() {

    private lateinit var viewModel: LoginViewModel
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var googleSignInLauncher: ActivityResultLauncher<Intent>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val authRepository = AuthRepository()
        val factory = LoginViewModelFactory(authRepository)
        viewModel = ViewModelProvider(this, factory).get(LoginViewModel::class.java)

        // Inizializza Google Sign-In
        googleSignInClient = authRepository.getGoogleSignInClient(requireActivity())

        // Registra ActivityResultLauncher per Google Sign-In
        googleSignInLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    val account = task.getResult(ApiException::class.java)
                    viewModel.signInWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
                    viewModel.clearError()
                    // Gestisci errore Google Sign-In
                }
            }
        }

        setupUI(view)
        observeViewModel()
    }

    private fun setupUI(view: View) {
        val emailEditText = view.findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = view.findViewById<EditText>(R.id.passwordEditText)
        val loginButton = view.findViewById<Button>(R.id.loginButton)
        val forgotPasswordTextView = view.findViewById<TextView>(R.id.forgotPasswordTextView)
        val backButton = view.findViewById<ImageView>(R.id.backButton)
        val googleButton = view.findViewById<SignInButton>(R.id.googleSignInButton)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            viewModel.signInWithEmail(email, password)
        }

        forgotPasswordTextView.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_resetPasswordFragment)
        }

        backButton.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_accessoFragment)
        }

        googleButton.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            googleSignInLauncher.launch(signInIntent)
        }
    }

    private fun observeViewModel() {
        viewModel.uiState.observe(viewLifecycleOwner) { state ->

            // Gestisci errori
            state.errorMessage?.let { message ->
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                viewModel.clearError()
            }

            // Gestisci navigazione
            if (state.shouldNavigateToHome) {
                findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                viewModel.navigationCompleted()
            }
        }
    }
}