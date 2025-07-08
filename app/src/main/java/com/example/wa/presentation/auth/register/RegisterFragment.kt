package com.example.wa.presentation.auth.register

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.wa.data.repository.AuthRepository
import com.example.wa.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.SignInButton

class RegisterFragment : Fragment() {

    private lateinit var viewModel: RegisterViewModel
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var googleSignInLauncher: ActivityResultLauncher<Intent>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.sing_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val authRepository = AuthRepository()
        val factory = RegisterViewModelFactory(authRepository)
        viewModel = ViewModelProvider(this, factory).get(RegisterViewModel::class.java)

        googleSignInClient = authRepository.getGoogleSignInClient(requireActivity())

        googleSignInLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    val account = task.getResult(ApiException::class.java)
                    viewModel.registerWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
                    viewModel.clearError()
                }
            }
        }

        setupUI(view)
        observeViewModel()
    }

    private fun setupUI(view: View) {
        val nameEditText = view.findViewById<EditText>(R.id.nameEditText)
        val emailEditText = view.findViewById<EditText>(R.id.mailEditText)
        val passwordEditText = view.findViewById<EditText>(R.id.passEditText)
        val repeatPasswordEditText = view.findViewById<EditText>(R.id.repeatPasswordEditText)
        val signUpButton = view.findViewById<Button>(R.id.signUpButton)
        val googleSignInButton = view.findViewById<SignInButton>(R.id.googleSignInButton)
        val backButton = view.findViewById<View>(R.id.backButton)

        signUpButton.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString()
            val repeatPassword = repeatPasswordEditText.text.toString()

            viewModel.registerWithEmail(name, email, password, repeatPassword)
        }

        googleSignInButton.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            googleSignInLauncher.launch(signInIntent)
        }

        backButton.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_accessoFragment)
        }
    }

    private fun observeViewModel() {
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            // Gestisci loading state
            if (state.isLoading) {
                // Mostra loading indicator
                // Potresti disabilitare i pulsanti per evitare doppi click
            }

            // Gestisci errori
            state.errorMessage?.let { message ->
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                viewModel.clearError()
            }

            // Gestisci navigazione
            if (state.shouldNavigateToHome) {
                findNavController().navigate(R.id.action_registerFragment_to_homeFragment)
                viewModel.navigationCompleted()
            }
        }
    }
}