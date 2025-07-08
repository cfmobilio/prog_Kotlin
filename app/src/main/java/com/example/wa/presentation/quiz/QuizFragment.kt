package com.example.wa.presentation.quiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wa.R
import android.widget.Toast

class QuizFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: QuizAdapter
    private lateinit var viewModel: QuizListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.quiz, container, false)

        viewModel = ViewModelProvider(this)[QuizListViewModel::class.java]

        setupRecyclerView(view)
        setupNavigationButtons(view)
        observeViewModel()

        return view
    }

    private fun setupRecyclerView(view: View) {
        recyclerView = view.findViewById(R.id.quiz)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = QuizAdapter(mutableListOf())
        recyclerView.adapter = adapter
    }

    private fun setupNavigationButtons(view: View) {
        val profileButton = view.findViewById<View>(R.id.profi)
        val homeButton = view.findViewById<View>(R.id.casa)
        val simButton = view.findViewById<View>(R.id.simulation)
        val insightButton = view.findViewById<View>(R.id.insight)
        val emergencyButton = view.findViewById<View>(R.id.eme)

        homeButton.setOnClickListener {
            findNavController().navigate(R.id.action_quizFragment_to_homeFragment)
        }
        profileButton.setOnClickListener {
            findNavController().navigate(R.id.action_quizFragment_to_profileFragment)
        }
        simButton.setOnClickListener {
            findNavController().navigate(R.id.action_quizFragment_to_simulationFragment)
        }
        insightButton.setOnClickListener {
            findNavController().navigate(R.id.action_quizFragment_to_extraFragment)
        }
        emergencyButton.setOnClickListener {
            findNavController().navigate(R.id.action_quizFragment_to_emergencyFragment)
        }
    }

    private fun observeViewModel() {
        viewModel.quizList.observe(viewLifecycleOwner) { quizList ->
            adapter.updateQuizList(quizList)
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshProgress()
    }
}