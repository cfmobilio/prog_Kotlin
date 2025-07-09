package com.example.wa.presentation.quiz

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.wa.R
import com.example.wa.presentation.profile.enableTTS

class BadFragment : Fragment(R.layout.bad_job) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListeners(view)
        enableTTS()
    }

    private fun setupClickListeners(view: View) {
        val rivediButton = view.findViewById<View>(R.id.reviewButton)
        val riprovaButton = view.findViewById<View>(R.id.retryButton)
        val profileButton = view.findViewById<View>(R.id.settingsButton)
        val backButton = view.findViewById<View>(R.id.backButton)
        val homeButton = view.findViewById<View>(R.id.casa)
        val quizButton = view.findViewById<View>(R.id.quiz)
        val simButton = view.findViewById<View>(R.id.simulation)
        val insightButton = view.findViewById<View>(R.id.insight)
        val emergencyButton = view.findViewById<View>(R.id.eme)

        rivediButton.setOnClickListener {
            findNavController().navigate(R.id.action_badFragment_to_homeFragment)
        }

        riprovaButton.setOnClickListener {
            // Ritorna al quiz per riprovare
            findNavController().navigate(R.id.action_badFragment_to_quizFragment)
        }

        profileButton.setOnClickListener {
            findNavController().navigate(R.id.action_badFragment_to_profileFragment)
        }

        backButton.setOnClickListener {
            findNavController().navigate(R.id.action_badFragment_to_quizFragment)
        }

        homeButton.setOnClickListener {
            findNavController().navigate(R.id.action_badFragment_to_homeFragment)
        }

        quizButton.setOnClickListener {
            findNavController().navigate(R.id.action_badFragment_to_quizFragment)
        }

        simButton.setOnClickListener {
            findNavController().navigate(R.id.action_badFragment_to_simulationFragment)
        }

        insightButton.setOnClickListener {
            findNavController().navigate(R.id.action_badFragment_to_extraFragment)
        }

        emergencyButton.setOnClickListener {
            findNavController().navigate(R.id.action_badFragment_to_emergencyFragment)
        }
    }
}