package com.example.wa.presentation.profile

import android.os.Bundle
import android.view.*
import android.widget.Switch
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.wa.R

class SettingsFragment : Fragment() {

    private val viewModel: SettingsViewModel by viewModels()
    private lateinit var switchAccessibility: Switch

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        switchAccessibility = view.findViewById(R.id.switchAccessibility)

        switchAccessibility.setOnCheckedChangeListener(null)
        switchAccessibility.isChecked = viewModel.accessibilityEnabled.value

        switchAccessibility.setOnCheckedChangeListener { _, isChecked ->
            viewModel.toggleAccessibility(isChecked)
            requireActivity().recreate()
        }
    }
}
