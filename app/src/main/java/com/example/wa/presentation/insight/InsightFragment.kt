package com.example.wa.presentation.insight

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.wa.R
import com.example.wa.data.model.NavigationDestination
import com.example.wa.data.model.NavigationEvent
import com.example.wa.databinding.InsightsBinding
import com.example.wa.presentation.profile.enableTTS

class InsightFragment : Fragment() {
    private lateinit var titoloTextView: TextView
    private lateinit var descrizioneTextView: TextView
    private val viewModel: InsightViewModel by viewModels()
    private lateinit var binding: InsightsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.insights, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Carica il contenuto dall'argomento
        val argomento = arguments?.getString("argomento") ?: "privacy"
        viewModel.loadInsight(argomento)

        setupViews(view)
        setupObservers()
        setupListeners()
        enableTTS()

    }

    private fun setupViews(view: View) {
        titoloTextView = view.findViewById(R.id.titoloTextView)
        descrizioneTextView = view.findViewById(R.id.descrizioneTextView)
    }

    private fun setupObservers() {
        viewModel.insight.observe(viewLifecycleOwner) { insight ->
            titoloTextView.text = insight?.titolo
            descrizioneTextView.text = insight?.descrizione
        }

        viewModel.navigationEvent.observe(viewLifecycleOwner) { event ->
            event?.let {
                handleNavigationEvent(it)
                viewModel.clearNavigationEvent()
            }
        }
    }


    private fun setupListeners() {
        binding.casa.setOnClickListener {
            viewModel.onNavigationClicked(NavigationDestination.Home)
        }
         binding.quiz.setOnClickListener {
            viewModel.onNavigationClicked(NavigationDestination.Quiz)
        }
        binding.simulation.setOnClickListener {
            viewModel.onNavigationClicked(NavigationDestination.Simulation)
        }
        binding.insight.setOnClickListener {
            viewModel.onNavigationClicked(NavigationDestination.Insight)
        }
        binding.eme.setOnClickListener {
            viewModel.onNavigationClicked(NavigationDestination.Emergency)
        }
        binding.backButton.setOnClickListener {
            findNavController().navigate(R.id.action_insightFragment_to_homeFragment)
        }
        binding.profileButton.setOnClickListener {
            viewModel.onNavigationClicked(NavigationDestination.Profile)
        }
    }

    private fun handleNavigationEvent(event: NavigationEvent) {
        when (event) {
            is NavigationEvent.NavigateToDestination -> {
                val actionId = when (event.destination) {
                    NavigationDestination.Home -> R.id.action_insightFragment_to_homeFragment
                    NavigationDestination.Quiz -> R.id.action_insightFragment_to_quizFragment
                    NavigationDestination.Simulation -> R.id.action_insightFragment_to_simulationFragment
                    ///NavigationDestination.Extra -> R.id.action_insightFragment_to_extraFragment
                    NavigationDestination.Emergency -> R.id.action_insightFragment_to_emergencyFragment
                    NavigationDestination.Profile -> R.id.action_insightFragment_to_profileFragment
                    //NavigationDestination.Back -> R.id.action_insightFragment_to_extraFragment
                    else -> return
                }
                findNavController().navigate(actionId)
            }
            else -> { return }
        }
    }
}