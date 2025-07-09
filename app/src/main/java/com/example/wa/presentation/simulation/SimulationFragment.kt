package com.example.wa.presentation.simulation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wa.R
import com.example.wa.data.repository.SimulationRepository
import com.example.wa.databinding.ExtraBinding
import com.example.wa.presentation.profile.enableTTS

class SimulationFragment : Fragment() {

    private var _binding: ExtraBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SimulationViewModel by viewModels()

    private lateinit var adapter: SimulationAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ExtraBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupObservers()
        setupNavigation()
        enableTTS()

    }

    private fun setupRecyclerView() {
        adapter = SimulationAdapter()
        binding.lista.layoutManager = LinearLayoutManager(requireContext())
        binding.lista.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.sim.observe(viewLifecycleOwner) { simulationList ->
            adapter.updateApps(simulationList)
        }

        viewModel.simulationMap.observe(viewLifecycleOwner) { map ->
            adapter.updateArgomentiMap(map)
        }
    }

    private fun setupNavigation() {
        binding.casa.setOnClickListener {
            findNavController().navigate(R.id.action_simulationFragment_to_homeFragment)
        }
        binding.quiz.setOnClickListener {
            findNavController().navigate(R.id.action_simulationFragment_to_quizFragment)
        }
        binding.insight.setOnClickListener {
            findNavController().navigate(R.id.action_simulationFragment_to_extraFragment)
        }
        binding.eme.setOnClickListener {
            findNavController().navigate(R.id.action_simulationFragment_to_emergencyFragment)
        }
        binding.profi.setOnClickListener {
            findNavController().navigate(R.id.action_simulationFragment_to_profileFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
