package com.example.wa.presentation.initialQuiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.wa.R
import com.example.wa.data.model.QuizResultLevel
import com.example.wa.data.model.QuizState

class QuestionsFragment : Fragment() {

    private val viewModel: QuizViewModel by viewModels()

    private lateinit var textTitoloDomanda: TextView
    private lateinit var textDomanda: TextView
    private lateinit var radioGroupOpzioni: RadioGroup
    private lateinit var opzione1: RadioButton
    private lateinit var opzione2: RadioButton
    private lateinit var opzione3: RadioButton
    private lateinit var buttonAvanti: Button
    private lateinit var buttonIndietro: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.questionnaire, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews(view)
        setupObservers()
        setupListeners()

        viewModel.loadQuestions()
    }

    private fun setupViews(view: View) {
        textTitoloDomanda = view.findViewById(R.id.textTitoloDomanda)
        textDomanda = view.findViewById(R.id.textDomanda)
        radioGroupOpzioni = view.findViewById(R.id.radioGroupOpzioni)
        opzione1 = view.findViewById(R.id.opzione1)
        opzione2 = view.findViewById(R.id.opzione2)
        opzione3 = view.findViewById(R.id.opzione3)
        buttonAvanti = view.findViewById(R.id.buttonAvanti)
        buttonIndietro = view.findViewById(R.id.buttonAvanti3)
    }

    private fun setupObservers() {
        viewModel.quizState.observe(viewLifecycleOwner) { state ->
            updateUI(state)
        }

        viewModel.navigationEvent.observe(viewLifecycleOwner) { level ->
            level?.let {
                navigateToResult(it)
                viewModel.clearNavigationEvent()
            }
        }
    }

    private fun setupListeners() {
        radioGroupOpzioni.setOnCheckedChangeListener { _, checkedId ->
            val answerIndex = when (checkedId) {
                R.id.opzione1 -> 0
                R.id.opzione2 -> 1
                R.id.opzione3 -> 2
                else -> -1
            }
            if (answerIndex != -1) {
                viewModel.selectAnswer(answerIndex)
            }
        }

        buttonAvanti.setOnClickListener {
            val currentState = viewModel.quizState.value
            if (currentState?.selectedAnswer != -1) {
                viewModel.nextQuestion()
            } else {
                Toast.makeText(requireContext(), "Seleziona una risposta!", Toast.LENGTH_SHORT).show()
            }
        }

        buttonIndietro.setOnClickListener {
            val currentState = viewModel.quizState.value
            if (currentState?.isFirstQuestion == true) {
                Toast.makeText(requireContext(), "Sei già alla prima domanda!", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.previousQuestion()
            }
        }
    }

    private fun updateUI(state: QuizState) {

        state.error?.let { error ->
            Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show()
        }

        state.currentQuestion?.let { question ->
            textTitoloDomanda.text = "Domanda ${state.currentQuestionIndex + 1}/${state.totalQuestions}"
            textDomanda.text = question.testo

            radioGroupOpzioni.clearCheck()

            opzione1.visibility = View.GONE
            opzione2.visibility = View.GONE
            opzione3.visibility = View.GONE

            question.opzioni.forEachIndexed { index, opzione ->
                when (index) {
                    0 -> {
                        opzione1.text = opzione
                        opzione1.visibility = View.VISIBLE
                    }
                    1 -> {
                        opzione2.text = opzione
                        opzione2.visibility = View.VISIBLE
                    }
                    2 -> {
                        opzione3.text = opzione
                        opzione3.visibility = View.VISIBLE
                    }
                }
            }

            buttonIndietro.visibility = if (state.isFirstQuestion) View.INVISIBLE else View.VISIBLE
            buttonAvanti.text = if (state.isLastQuestion) "Fine" else "Avanti"
        }
    }

    private fun navigateToResult(level: QuizResultLevel) {
        val action = when (level) {
            QuizResultLevel.BASE -> R.id.action_questionsFragment_to_base_result
            QuizResultLevel.INTERMEDIATE -> R.id.action_questionsFragment_to_intermediate_result
            QuizResultLevel.ADVANCED -> R.id.action_questionsFragment_to_advanced_result
        }
        findNavController().navigate(action)
    }


}