package com.example.wa

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.core.content.edit
import com.example.wa.presentation.profile.enableTTS

class SplashFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.start_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.postDelayed({
            val prefs = requireActivity().getSharedPreferences("WebAwarePrefs", Context.MODE_PRIVATE)
            val isFirstLaunch = prefs.getBoolean("isFirstLaunch", true)

            if (isFirstLaunch) {
                prefs.edit { putBoolean("isFirstLaunch", false) }
                Log.d("SplashFragment", "Sto facendo il primo accesso")
                findNavController().navigate(R.id.action_splashFragment_to_quizIntroFragment)
            } else {
                Log.e("SplashFragment", "Sto facendo il n+1 accesso")
                findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
            }
        }, 2000)
        enableTTS()

    }
}
