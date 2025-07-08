package com.example.wa.presentation.emergency

import android.graphics.Color
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

class EmergencyFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: EmergencyAdapter
    private val viewModel: EmergencyViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Rende la status bar trasparente
        requireActivity().window.statusBarColor = Color.TRANSPARENT

        val view = inflater.inflate(R.layout.emergency, container, false)

        setupViews(view)
        setupRecyclerView()
        setupObservers()
        setupNavigation(view)

        return view
    }

    private fun setupViews(view: View) {
        recyclerView = view.findViewById(R.id.recyclerViewEmergenze)
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = EmergencyAdapter(emptyList())
        recyclerView.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.emergencies.observe(viewLifecycleOwner) { emergencies ->
            adapter.updateEmergencies(emergencies)
        }
    }

    private fun setupNavigation(view: View) {
        val profileButton = view.findViewById<View>(R.id.profi)
        val homeButton = view.findViewById<View>(R.id.casa)
        val quizButton = view.findViewById<View>(R.id.quiz)
        val simButton = view.findViewById<View>(R.id.simulation)
        val insightButton = view.findViewById<View>(R.id.insight)

        profileButton.setOnClickListener {
            findNavController().navigate(R.id.action_emergencyFragment_to_profileFragment)
        }
        homeButton.setOnClickListener {
            findNavController().navigate(R.id.action_emergencyFragment_to_homeFragment)
        }
        quizButton.setOnClickListener {
            findNavController().navigate(R.id.action_emergencyFragment_to_quizFragment)
        }
        simButton.setOnClickListener {
            findNavController().navigate(R.id.action_extraFragment_to_simulationFragment)
        }
        insightButton.setOnClickListener {
            findNavController().navigate(R.id.action_emergencyFragment_to_extraFragment)
        }
    }
}