package com.example.wa.presentation.auth.register

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.wa.R
import com.example.wa.presentation.auth.register.RegisterViewModel.RegisterState
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.flow.collectLatest

class RegisterFragment : Fragment() {

    private val viewModel: RegisterViewModel by viewModels()
    private lateinit var googleSignInClient: GoogleSignInClient

    private val googleSignInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                Log.d("GOOGLE_LOGIN", "Google account: ${account.email}")
                Toast.makeText(requireContext(), "Account Google: ${account.email}", Toast.LENGTH_SHORT).show()
                viewModel.registerWithGoogle(account)
            } catch (e: ApiException) {
                Log.e("GOOGLE_LOGIN", "Google SignIn fallito: ${e.statusCode} - ${e.message}")
                Toast.makeText(requireContext(), "Errore Google: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        } else {
            Log.e("GOOGLE_LOGIN", "Sign-In cancellato o fallito (code=${result.resultCode})")
            Toast.makeText(requireContext(), "Google Sign-In annullato o fallito", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.sing_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Google config
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        val nameEditText = view.findViewById<EditText>(R.id.nameEditText)
        val emailEditText = view.findViewById<EditText>(R.id.mailEditText)
        val passwordEditText = view.findViewById<EditText>(R.id.passEditText)
        val repeatPasswordEditText = view.findViewById<EditText>(R.id.repeatPasswordEditText)
        val signUpButton = view.findViewById<Button>(R.id.signUpButton)
        val googleButton = view.findViewById<SignInButton>(R.id.googleSignInButton)
        val backButton = view.findViewById<View>(R.id.backButton)

        signUpButton.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString()
            val repeatPassword = repeatPasswordEditText.text.toString()

            if (name.isBlank() || email.isBlank() || password.isBlank() || repeatPassword.isBlank()) {
                Toast.makeText(requireContext(), "Completa tutti i campi", Toast.LENGTH_SHORT).show()
            } else if (password != repeatPassword) {
                Toast.makeText(requireContext(), "Le password non coincidono", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.registerWithEmail(name, email, password)
            }
        }

        googleButton.setOnClickListener {
            val intent = googleSignInClient.signInIntent
            googleSignInLauncher.launch(intent)
        }

        backButton.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_accessoFragment)
        }

        lifecycleScope.launchWhenStarted {
            viewModel.registerState.collectLatest { state ->
                when (state) {
                    is RegisterState.Loading -> {
                        // mostra spinner se vuoi
                    }
                    is RegisterState.Success -> {
                        Toast.makeText(requireContext(), "Registrazione completata!", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_registerFragment_to_homeFragment)
                    }
                    is RegisterState.Error -> {
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> {}
                }
            }
        }
    }
}
