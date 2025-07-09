package com.example.wa.presentation.profile

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.wa.R

class SupportFragment : Fragment() {

    private val viewModel: SupportViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.support_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val emailButton = view.findViewById<Button>(R.id.emailSupportButton)
        val sendFeedbackButton = view.findViewById<Button>(R.id.sendFeedbackButton)
        val ratingBar = view.findViewById<RatingBar>(R.id.appRatingBar)
        val feedbackInput = view.findViewById<EditText>(R.id.feedbackInput)

        emailButton.setOnClickListener {
            viewModel.sendEmail(requireContext())
        }

        sendFeedbackButton.setOnClickListener {
            viewModel.validateAndSubmitFeedback(
                context = requireContext(),
                rating = ratingBar.rating.toInt(),
                feedback = feedbackInput.text.toString()
            ) {
                ratingBar.rating = 0f
                feedbackInput.text.clear()
            }
        }

        enableTTS()
    }
}
