package com.example.wa.presentation.simulation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
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
        binding.viewModel = viewModel // opzionale, se hai variabili nel layout
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
            viewModel.onAnswerSelected(true)
        }

        binding.buttonSbagliato.setOnClickListener {
            viewModel.onAnswerSelected(false)
        }
    }

    private fun observeUiState() {
        lifecycleScope.launch {
            viewModel.uiState.collectLatest { state ->
                binding.textTitoloDomanda.text = state.simulazione?.titolo ?: ""
                binding.textDomanda.text = state.simulazione?.descrizione ?: ""
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
