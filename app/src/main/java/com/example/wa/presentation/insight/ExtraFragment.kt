package com.example.wa.presentation.insight

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wa.R
import com.example.wa.databinding.ExtraBinding

class ExtraFragment : Fragment() {
    private var _binding: ExtraBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ExtraViewModel by viewModels()
    private lateinit var adapter: ExtraAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ExtraBinding.inflate(inflater, container, false)

        setupRecyclerView()
        setupObservers()
        setupNavigation()

        return binding.root
    }

    private fun setupRecyclerView() {
        // Inizializzo l'adapter senza dati
        adapter = ExtraAdapter()
        binding.lista.layoutManager = LinearLayoutManager(requireContext())
        binding.lista.adapter = adapter
    }

    private fun setupObservers() {
        // Osservo la lista di apps dal ViewModel e aggiorno l'adapter
        viewModel.apps.observe(viewLifecycleOwner) { appsList ->
            adapter.updateApps(appsList)
        }

        // Osservo la mappa degli argomenti dal ViewModel e aggiorno l'adapter
        viewModel.argomentiMap.observe(viewLifecycleOwner) { map ->
            adapter.updateArgomentiMap(map)
        }
    }

    private fun setupNavigation() {
        binding.quiz.setOnClickListener {
            findNavController().navigate(R.id.action_extraFragment_to_quizFragment)
        }

        binding.simulation.setOnClickListener {
            findNavController().navigate(R.id.action_extraFragment_to_simulationFragment)
        }

        binding.eme.setOnClickListener {
            findNavController().navigate(R.id.action_extraFragment_to_emergency)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
