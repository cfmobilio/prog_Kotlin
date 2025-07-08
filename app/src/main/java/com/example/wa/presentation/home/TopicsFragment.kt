package com.example.wa.presentation.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.wa.R

class TopicsFragment : Fragment() {

    private lateinit var titoloTextView: TextView
    private lateinit var descrizioneTextView: TextView
    private lateinit var thumbnailImageView: ImageView
    private lateinit var continuaButton: Button

    private val viewModel: TopicsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.topics, container, false)

        setupViews(view)
        setupObservers()
        setupNavigation(view)

        // Carica i dati
        val tipo = arguments?.getString("argomento") ?: "privacy"
        viewModel.loadTopic(tipo)

        return view
    }

    private fun setupViews(view: View) {
        titoloTextView = view.findViewById(R.id.titoloTextView)
        descrizioneTextView = view.findViewById(R.id.descrizioneTextView)
        thumbnailImageView = view.findViewById(R.id.videoThumbnail)
        continuaButton = view.findViewById(R.id.continuaButton)
    }

    private fun setupObservers() {
        viewModel.topic.observe(viewLifecycleOwner) { topic ->
            titoloTextView.text = topic.titolo
            descrizioneTextView.text = topic.descrizione

            if (topic.videoUrl.isNotEmpty()) {
                setupVideoThumbnail(topic.videoUrl)
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            if (error != null) {
                // Gestisci l'errore (toast, snackbar, etc.)
            }
        }
    }

    private fun setupVideoThumbnail(videoUrl: String) {
        val videoId = Uri.parse(videoUrl).getQueryParameter("v")
        if (videoId != null) {
            val thumbnailUrl = "https://img.youtube.com/vi/$videoId/hqdefault.jpg"
            Glide.with(this)
                .load(thumbnailUrl)
                .into(thumbnailImageView)

            thumbnailImageView.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl))
                startActivity(intent)
            }
        }
    }

    private fun setupNavigation(view: View) {
        val backButton = view.findViewById<View>(R.id.backButton)
        val profileButton = view.findViewById<View>(R.id.profileButton)
        val homeButton = view.findViewById<View>(R.id.casa)
        val quizButton = view.findViewById<View>(R.id.quiz)
        val simulationButton = view.findViewById<View>(R.id.simulation)
        val insightButton = view.findViewById<View>(R.id.insight)
        val emergencyButton = view.findViewById<View>(R.id.eme)

        continuaButton.setOnClickListener {
            findNavController().navigate(R.id.action_topicsFragment_to_quizFragment)
        }

        backButton.setOnClickListener {
            findNavController().navigate(R.id.action_topicsFragment_to_homeFragment)
        }

//        profileButton.setOnClickListener {
//            findNavController().navigate(R.id.action_topicsFragment_to_profileFragment)
//        }

        homeButton.setOnClickListener {
            findNavController().navigate(R.id.action_topicsFragment_to_homeFragment)
        }

        quizButton.setOnClickListener {
            findNavController().navigate(R.id.action_topicsFragment_to_quizFragment)
        }

        simulationButton.setOnClickListener {
            findNavController().navigate(R.id.action_topicsFragment_to_simulationFragment)
        }

        insightButton.setOnClickListener {
            findNavController().navigate(R.id.action_topicsFragment_to_extraFragment)
        }

        emergencyButton.setOnClickListener {
            findNavController().navigate(R.id.action_topicsFragment_to_emergency)
        }
    }
}