package com.example.wa.presentation.home

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
import com.example.wa.presentation.profile.enableTTS

class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SubjectAdapter
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.home_fragment, container, false)

        setupViews(view)
        setupRecyclerView()
        setupObservers()
        setupNavigation(view)
        enableTTS()

        return view
    }

    private fun setupViews(view: View) {
        recyclerView = view.findViewById(R.id.recyclerView)
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = SubjectAdapter(emptyList())
        recyclerView.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.subjects.observe(viewLifecycleOwner) { subjects ->
            adapter.updateSubjects(subjects)
        }

        viewModel.argomentiMap.observe(viewLifecycleOwner) { map ->
            adapter.updateArgomentiMap(map)
        }
    }

    private fun setupNavigation(view: View) {
        val profileButton = view.findViewById<View>(R.id.profilo)
        val quizButton = view.findViewById<View>(R.id.quiz)
        val simButton = view.findViewById<View>(R.id.simulation)
        val insightButton = view.findViewById<View>(R.id.insight)
        val emergencyButton = view.findViewById<View>(R.id.eme)

        profileButton.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_profileFragment)
        }

        quizButton.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_quizFragment)
        }

        simButton.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_simulationFragment)
        }

        insightButton.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_extraFragment)
        }

        emergencyButton.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_emergency)
        }
    }
}