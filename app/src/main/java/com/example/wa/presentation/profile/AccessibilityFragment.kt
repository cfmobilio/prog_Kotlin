package com.example.wa.presentation.profile

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.wa.MainActivity
import com.example.wa.R
import kotlinx.coroutines.flow.collectLatest

class AccessibilityFragment : Fragment() {

    private val viewModel: AccessibilityViewModel by viewModels()
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

        // Applica le dimensioni del testo se necessario
        view.applyAccessibilityTextSize(requireContext())

        val prefs = requireContext().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val highContrastEnabled = prefs.getBoolean("accessibility_mode", false)

        switchContrast = view.findViewById(R.id.switch_high_contrast)
        buttonRead = view.findViewById(R.id.button_read_text)

        switchContrast.isChecked = highContrastEnabled
        switchContrast.setOnCheckedChangeListener { _, isChecked ->
            // CORREZIONE: underscore invece di asterisco
            prefs.edit().putBoolean("accessibility_mode", isChecked).apply()
            (activity as? MainActivity)?.applyHighContrast(isChecked)
        }

        viewModel.initTTS()

        buttonRead.setOnClickListener {
            viewModel.speak("Esempio di testo letto da Text to Speech")
        }

        lifecycleScope.launchWhenStarted {
            viewModel.isTtsReady.collectLatest { ready ->
                if (!ready) {
                    Toast.makeText(context, "Lingua non supportata o errore TTS", Toast.LENGTH_SHORT).show()
                }
            }
        }

        switchTts = view.findViewById(R.id.switch_tts)
        val ttsEnabled = prefs.getBoolean("tts_enabled", false)
        switchTts.isChecked = ttsEnabled

        if (ttsEnabled) {
            viewModel.initTTS()
        }

        switchTts.setOnCheckedChangeListener { _, isChecked ->
            // CORREZIONE: underscore invece di asterisco
            prefs.edit().putBoolean("tts_enabled", isChecked).apply()
            if (isChecked) {
                viewModel.initTTS()
            } else {
                viewModel.releaseTTS()
            }
        }

        switchLargeText = view.findViewById(R.id.switch_big_text)
        val isLargeText = prefs.getBoolean("large_text", false)
        switchLargeText.isChecked = isLargeText

        switchLargeText.setOnCheckedChangeListener { _, isChecked ->
            // CORREZIONE: underscore invece di asterisco
            prefs.edit().putBoolean("large_text", isChecked).apply()
            // Usa il metodo corretto di MainActivity
            (activity as? MainActivity)?.updateLargeText(isChecked)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.releaseTTS()
    }
}