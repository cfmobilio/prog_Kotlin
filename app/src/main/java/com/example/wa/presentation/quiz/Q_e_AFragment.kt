package com.example.wa.presentation.quiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.wa.R
import com.example.wa.di.QuizQuestionViewModelFactory
import android.widget.Toast


class Q_e_AFragment : Fragment() {
    private lateinit var viewModel: QuizQuestionViewModel
    private lateinit var argomento: String

    // UI elements
    private lateinit var textTitoloDomanda: TextView
    private lateinit var textDomanda: TextView
    private lateinit var radioGroupOpzioni: RadioGroup
    private lateinit var opzione1: RadioButton
    private lateinit var opzione2: RadioButton
    private lateinit var opzione3: RadioButton
    private lateinit var buttonAvanti: Button
    private lateinit var buttonIndietro: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.questions, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        argomento = arguments?.getString("argomento") ?: return

        initializeViewModel()
        initializeViews(view)
        setupClickListeners(view)
        observeViewModel()
    }

    private fun initializeViewModel() {
        val factory = QuizQuestionViewModelFactory(argomento)
        viewModel = ViewModelProvider(this, factory)[QuizQuestionViewModel::class.java]
    }

    private fun initializeViews(view: View) {
        textTitoloDomanda = view.findViewById(R.id.textTitoloDomanda)
        textDomanda = view.findViewById(R.id.textDomanda)
        radioGroupOpzioni = view.findViewById(R.id.radioGroupOpzioni)
        opzione1 = view.findViewById(R.id.opzione1)
        opzione2 = view.findViewById(R.id.opzione2)
        opzione3 = view.findViewById(R.id.opzione3)
        buttonAvanti = view.findViewById(R.id.buttonAvanti)
        buttonIndietro = view.findViewById(R.id.buttonIndietro)
    }

    private fun setupClickListeners(view: View) {
        view.findViewById<View>(R.id.backbutton).setOnClickListener {
            viewModel.saveCurrentProgress()
            findNavController().navigate(R.id.action_domandeFragment_to_quizFragment)
        }

        buttonAvanti.setOnClickListener {
            val selectedId = radioGroupOpzioni.checkedRadioButtonId
            if (selectedId != -1) {
                val rispostaSelezionata = when (selectedId) {
                    R.id.opzione1 -> 0
                    R.id.opzione2 -> 1
                    R.id.opzione3 -> 2
                    else -> -1
                }
                viewModel.submitAnswer(rispostaSelezionata)
            } else {
                Toast.makeText(requireContext(), "Seleziona una risposta!", Toast.LENGTH_SHORT).show()
            }
        }

        buttonIndietro.setOnClickListener {
            viewModel.goToPreviousQuestion()
        }
    }

    private fun observeViewModel() {
        viewModel.domande.observe(viewLifecycleOwner) { domande ->
            // Questions loaded, UI will be updated when currentQuestionIndex changes
        }

        viewModel.currentQuestionIndex.observe(viewLifecycleOwner) { index ->
            showQuestion(index)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            // Show/hide loading indicator
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.quizCompleted.observe(viewLifecycleOwner) { completed ->
            if (completed) {
                navigateToResult()
            }
        }
    }

    private fun showQuestion(index: Int) {
        val domande = viewModel.domande.value ?: return
        if (index >= domande.size) return

        val domanda = domande[index]
        textTitoloDomanda.text = "Domanda ${index + 1}/${domande.size}"
        textDomanda.text = domanda.testo
        radioGroupOpzioni.clearCheck()

        listOf(opzione1, opzione2, opzione3).forEach { it.visibility = View.GONE }

        domanda.opzioni.forEachIndexed { i, testo ->
            when (i) {
                0 -> {
                    opzione1.text = testo
                    opzione1.visibility = View.VISIBLE
                }
                1 -> {
                    opzione2.text = testo
                    opzione2.visibility = View.VISIBLE
                }
                2 -> {
                    opzione3.text = testo
                    opzione3.visibility = View.VISIBLE
                }
            }
        }

        buttonIndietro.visibility = if (index == 0) View.INVISIBLE else View.VISIBLE
        buttonAvanti.text = if (index == domande.size - 1) "Fine" else "Avanti"
    }

    private fun navigateToResult() {
        val percentage = viewModel.getFinalPercentage()
        val bundle = Bundle().apply {
            putString("quizId", argomento)
            putInt("percentage", percentage)
        }

        when {
            percentage >= 80 -> findNavController().navigate(R.id.action_domandeFragment_to_goodFragment, bundle)
            else -> findNavController().navigate(R.id.action_domandeFragment_to_badFragment, bundle)
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.saveCurrentProgress()
    }
}