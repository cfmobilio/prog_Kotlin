package com.example.wa.presentation.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.wa.R
import com.example.wa.data.model.NavigationDestination
import com.example.wa.data.model.NavigationEvent
import com.example.wa.data.repository.ProfileRepository
import com.example.wa.di.ProfileViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.example.wa.data.model.ProfileData
import com.example.wa.data.model.ProgressData

class ProfileFragment : Fragment() {

    private lateinit var viewModel: ProfileViewModel
    private lateinit var repository: ProfileRepository
    private lateinit var viewModelFactory: ProfileViewModelFactory

    // UI Components
    private lateinit var profileName: TextView
    private lateinit var profileImage: ImageView
    private lateinit var backButton: ImageView
    private lateinit var settingsButton: ImageView
    private lateinit var iconGrid: GridLayout
    private lateinit var progressBar: ProgressBar
    private lateinit var progressText: TextView

    private val badgeViews = mutableMapOf<String, ImageView>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()
        initViews(view)
        setupClickListeners(view)
        observeViewModel()

        enableTTS()

    }

    private fun setupViewModel() {
        repository = ProfileRepository()
        viewModelFactory = ProfileViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[ProfileViewModel::class.java]
    }

    private fun initViews(view: View) {
        profileName = view.findViewById(R.id.profileName)
        profileImage = view.findViewById(R.id.profileImage)
        backButton = view.findViewById(R.id.backButton)
        settingsButton = view.findViewById(R.id.settingsButton)
        iconGrid = view.findViewById(R.id.iconGrid)
        progressBar = view.findViewById(R.id.profileProgressBar)
        progressText = view.findViewById(R.id.progressText)

        initBadgeViews(view)
    }

    private fun initBadgeViews(view: View) {
        val badgeIds = mapOf(
            "lock" to R.id.badge_lock,
            "banned" to R.id.badge_banned,
            "target" to R.id.badge_target,
            "eyes" to R.id.badge_eyes,
            "fact_check" to R.id.badge_fact_check,
            "key" to R.id.badge_key,
            "private_detective" to R.id.badge_private_detective,
            "floppy_disk" to R.id.badge_floppy_disk,
            "earth" to R.id.badge_earth,
            "compass" to R.id.badge_compass,
            )

        badgeIds.forEach { (key, resId) ->
            view.findViewById<ImageView>(resId)?.let {
                badgeViews[key] = it
                it.setOnClickListener { _ -> viewModel.onBadgeClicked(key) }
            }
        }
    }

    private fun setupClickListeners(view: View) {
        backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        settingsButton.setOnClickListener {
            showOverflowMenu(it)
        }

        // Navigation buttons
        view.findViewById<View>(R.id.casa).setOnClickListener {
            viewModel.onNavigationClicked(NavigationDestination.Home)
        }
        view.findViewById<View>(R.id.quiz).setOnClickListener {
            viewModel.onNavigationClicked(NavigationDestination.Quiz)
        }
        view.findViewById<View>(R.id.simulation).setOnClickListener {
            viewModel.onNavigationClicked(NavigationDestination.Simulation)
        }
        view.findViewById<View>(R.id.insight).setOnClickListener {
            viewModel.onNavigationClicked(NavigationDestination.Insight)
        }
        view.findViewById<View>(R.id.eme).setOnClickListener {
            viewModel.onNavigationClicked(NavigationDestination.Emergency)
        }
    }

    private fun observeViewModel() {
        viewModel.profileData.observe(viewLifecycleOwner) { profileData ->
            updateProfile(profileData)
        }

        viewModel.badges.observe(viewLifecycleOwner) { badges ->
            updateBadges(badges)
        }

        viewModel.progressData.observe(viewLifecycleOwner) { progressData ->
            updateProgressBar(progressData)
        }

        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            handleUiState(uiState)
        }

        viewModel.navigationEvent.observe(viewLifecycleOwner) { event ->
            event?.let {
                handleNavigationEvent(it)
                viewModel.onNavigationEventHandled()
            }
        }

        viewModel.toastMessage.observe(viewLifecycleOwner) { message ->
            message?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                viewModel.onToastMessageHandled()
            }
        }
    }

    private fun updateProfile(profileData: ProfileData) {
        profileName.text = profileData.name
        profileImage.setImageResource(R.drawable.fox_logo)
    }

    private fun updateBadges(badges: Map<String, Boolean>) {
        badgeViews.forEach { (id, view) ->
            val unlocked = badges[id] == true
            val drawableRes = resources.getIdentifier(id, "drawable", requireContext().packageName)
            view.setImageResource(drawableRes)
            view.alpha = if (unlocked) 1.0f else 0.3f
        }
    }

    private fun updateProgressBar(progressData: ProgressData) {
        progressBar.progress = progressData.percentage
        progressText.text = "${progressData.unlocked}/${progressData.total} badge sbloccati (${progressData.percentage}%)"
    }

    private fun handleUiState(uiState: ProfileUiState) {
        when (uiState) {
            is ProfileUiState.Loading -> {
                // Mostra loading indicator se necessario
            }
            is ProfileUiState.Success -> {
                // Nasconde loading indicator se necessario
            }
            is ProfileUiState.Error -> {
                Toast.makeText(context, uiState.message, Toast.LENGTH_SHORT).show()
                if (uiState.message.contains("autenticato")) {
                    findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
                }
            }
        }
    }

    private fun handleNavigationEvent(event: NavigationEvent) {
        when (event) {
            is NavigationEvent.NavigateToDestination -> {
                when (event.destination) {
                    NavigationDestination.Home ->
                        findNavController().navigate(R.id.action_profileFragment_to_homeFragment)
                    NavigationDestination.Quiz ->
                        findNavController().navigate(R.id.action_profileFragment_to_quizFragment)
                    NavigationDestination.Simulation ->
                        findNavController().navigate(R.id.action_profileFragment_to_simulationFragment)
                    NavigationDestination.Insight ->
                        findNavController().navigate(R.id.action_profileFragment_to_extraFragment)
                    NavigationDestination.Emergency ->
                        findNavController().navigate(R.id.action_profileFragment_to_emergency)
                    NavigationDestination.Profile -> {
                        // Caso logout - naviga al login
                        findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
                    }
                    else -> {
                        // Gestisci altre destinazioni se necessario
                    }
                }
            }
            is NavigationEvent.NavigateToInsight -> {
                // Gestisci la navigazione con argomento se necessario
                findNavController().navigate(R.id.action_profileFragment_to_extraFragment)
            }
        }
    }

    private fun showOverflowMenu(anchor: View) {
        val popup = PopupMenu(requireContext(), anchor)
        popup.menuInflater.inflate(R.menu.profile_menu, popup.menu)
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_logout -> {
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle("Logout")
                        .setMessage("Vuoi davvero uscire dal tuo account?")
                        .setPositiveButton("Sì") { _, _ ->
                            viewModel.logout()
                        }
                        .setNegativeButton("Annulla", null)
                        .show()
                    true
                }
                R.id.menu_support -> {
                    // Gestisci la navigazione al supporto direttamente nel fragment
                    findNavController().navigate(R.id.action_profileFragment_to_supportFragment)
                    true
                }
                R.id.accessibility -> {
                    // Gestisci la navigazione all'accessibilità direttamente nel fragment
                    findNavController().navigate(R.id.action_profileFragment_to_accesibility)
                    true
                }
                else -> false
            }
        }
        popup.show()
    }
}