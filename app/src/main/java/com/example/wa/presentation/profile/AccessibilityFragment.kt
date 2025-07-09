// AccessibilityFragment.kt - Versione semplificata
package com.example.wa.presentation.profile

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.wa.MainActivity
import com.example.wa.R

class AccessibilityFragment : Fragment() {

    private lateinit var switchContrast: Switch
    private lateinit var buttonRead: Button
    private lateinit var switchLargeText: Switch
    private lateinit var switchTts: Switch

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_accessibility, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Applica accessibilità
        view.applyAccessibilityTextSize(requireContext())

        // ABILITA TTS - UNA SOLA RIGA!
        enableTTS()

        val prefs = requireContext().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)

        setupSwitches(view, prefs)
        setupNavigation(view)
    }

    private fun setupSwitches(view: View, prefs: android.content.SharedPreferences) {
        // High Contrast
        switchContrast = view.findViewById(R.id.switch_high_contrast)
        switchContrast.isChecked = prefs.getBoolean("accessibility_mode", false)
        switchContrast.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("accessibility_mode", isChecked).apply()
            (activity as? MainActivity)?.applyHighContrast(isChecked)
        }

        // TTS
        switchTts = view.findViewById(R.id.switch_tts)
        switchTts.isChecked = prefs.getBoolean("tts_enabled", false)
        switchTts.setOnCheckedChangeListener { _, isChecked ->
            TTSHelper.setEnabled(isChecked)
        }

        // Button test
        buttonRead = view.findViewById(R.id.button_read_text)
        buttonRead.setOnClickListener {
            TTSHelper.speak("Esempio di testo letto da Text to Speech")
        }

        // Large Text
        switchLargeText = view.findViewById(R.id.switch_big_text)
        switchLargeText.isChecked = prefs.getBoolean("large_text", false)
        switchLargeText.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("large_text", isChecked).apply()
            (activity as? MainActivity)?.updateLargeText(isChecked)
        }
    }

    private fun setupNavigation(view: View) {
        val backButton = view.findViewById<ImageView>(R.id.backButton)
        backButton.setOnClickListener {
            findNavController().navigate(R.id.action_accessibilityFragment_to_profileFragment)
        }
    }
}