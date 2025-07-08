package com.example.wa.presentation.quiz

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.wa.R

class GoodFragment : Fragment(R.layout.good_job) {
    private lateinit var badgeViewModel: BadgeViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        badgeViewModel = ViewModelProvider(this)[BadgeViewModel::class.java]

        setupClickListeners(view)
        observeViewModel()

        // Handle badge unlocking
        val quizId = arguments?.getString("quizId")
        val percentage = arguments?.getInt("percentage", 0) ?: 0

        if (quizId != null && percentage >= 80) {
            badgeViewModel.unlockBadge(quizId)
        }
    }

    private fun setupClickListeners(view: View) {
        val shareButton: Button = view.findViewById(R.id.shareButton)
        val viewBadgeButton: Button = view.findViewById(R.id.viewBadgeButton)
        val nextQuizButton: Button = view.findViewById(R.id.nextQuizButton)
        val profileButton = view.findViewById<View>(R.id.settingsButton)
        val backButton = view.findViewById<View>(R.id.backButton)
        val homeButton = view.findViewById<View>(R.id.casa)
        val quizButton = view.findViewById<View>(R.id.quiz)
        val simButton = view.findViewById<View>(R.id.simulation)
        val insightButton = view.findViewById<View>(R.id.insight)
        val emergencyButton = view.findViewById<View>(R.id.eme)

        shareButton.setOnClickListener {
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "Ho appena sbloccato un nuovo badge su WebAware! 🥳")
                type = "text/plain"
            }
            startActivity(Intent.createChooser(shareIntent, "Condividi con..."))
        }

        viewBadgeButton.setOnClickListener {
            findNavController().navigate(R.id.action_goodFragment_to_profileFragment)
        }

        nextQuizButton.setOnClickListener {
            findNavController().navigate(R.id.action_goodFragment_to_quizFragment)
        }

        profileButton.setOnClickListener {
            findNavController().navigate(R.id.action_goodFragment_to_profileFragment)
        }

        backButton.setOnClickListener {
            findNavController().navigate(R.id.action_goodFragment_to_quizFragment)
        }

        homeButton.setOnClickListener {
            findNavController().navigate(R.id.action_goodFragment_to_homeFragment)
        }

        quizButton.setOnClickListener {
            findNavController().navigate(R.id.action_goodFragment_to_quizFragment)
        }

        simButton.setOnClickListener {
            findNavController().navigate(R.id.action_goodFragment_to_simulationFragment)
        }

        insightButton.setOnClickListener {
            findNavController().navigate(R.id.action_goodFragment_to_extraFragment)
        }

        emergencyButton.setOnClickListener {
            findNavController().navigate(R.id.action_quizFragment_to_emergencyFragment)
        }
    }

    private fun observeViewModel() {
        badgeViewModel.badgeUnlocked.observe(viewLifecycleOwner) { unlocked ->
            if (unlocked) {
                Log.d("GoodFragment", "Badge sbloccato con successo")
            }
        }

        badgeViewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Log.e("GoodFragment", "Errore nello sbloccare il badge: $it")
            }
        }
    }
}