package com.example.wa.presentation.simulation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.wa.R
import com.example.wa.databinding.SimulationsBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SituationFragment : Fragment() {

    private val viewModel: SituationViewModel by viewModels()
    private var _binding: SimulationsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SimulationsBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val argomento = arguments?.getString("argomento") ?: "privacy"
        viewModel.loadSimulazione(argomento)

        setupListeners()
        observeUiState()
    }

    private fun setupListeners() {
        binding.backbutton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.buttonCorretto.setOnClickListener {
            viewModel.checkAnswerByCorrectness(true)
        }

        binding.buttonSbagliato.setOnClickListener {
            viewModel.checkAnswerByCorrectness(false)
        }
    }

    private fun observeUiState() {
        lifecycleScope.launch {
            viewModel.uiState.collectLatest { state ->
                binding.textTitoloDomanda.text = state.simulazione?.titolo ?: ""
                binding.textDomanda.text = state.simulazione?.descrizione ?: ""

                state.feedback?.let { feedback ->
                    showFeedbackDialog(feedback, state.isAnswerCorrect ?: false)
                }

                binding.buttonCorretto.isEnabled = !state.isLoading
                binding.buttonSbagliato.isEnabled = !state.isLoading
            }
        }
    }

    private fun showFeedbackDialog(feedback: String, isCorrect: Boolean) {
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(if (isCorrect) "Risposta Corretta! ✅" else "Risposta Sbagliata ❌")
            .setMessage(feedback)
            .setPositiveButton("Continua") { dialog, _ ->
                dialog.dismiss()
            }
            .setCancelable(false)
            .create()

        dialog.show()

        dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setTextColor(
            ContextCompat.getColor(requireContext(),
                if (isCorrect) R.color.green else R.color.red
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}