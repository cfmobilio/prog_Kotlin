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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_accessibility, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val prefs = requireContext().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val highContrastEnabled = prefs.getBoolean("accessibility_mode", false)

        switchContrast = view.findViewById(R.id.switch_high_contrast)
        buttonRead = view.findViewById(R.id.button_read_text)

        switchContrast.isChecked = highContrastEnabled
        switchContrast.setOnCheckedChangeListener { _, isChecked ->
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.releaseTTS()
    }
}
