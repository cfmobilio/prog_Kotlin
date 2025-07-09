package com.example.wa.presentation.quiz

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.wa.R
import com.example.wa.data.model.Question
import com.example.wa.presentation.profile.enableTTS
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.FieldPath

class Q_e_AFragment : Fragment() {

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private val args: Q_e_AFragmentArgs by navArgs()
    private val argomento by lazy { args.argomento }

    private var domande = mutableListOf<Question>()
    private var domandaCorrente = 0
    private var punteggio = 0

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

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        // Inizializza UI
        textTitoloDomanda = view.findViewById(R.id.textTitoloDomanda)
        textDomanda = view.findViewById(R.id.textDomanda)
        radioGroupOpzioni = view.findViewById(R.id.radioGroupOpzioni)
        opzione1 = view.findViewById(R.id.opzione1)
        opzione2 = view.findViewById(R.id.opzione2)
        opzione3 = view.findViewById(R.id.opzione3)
        buttonAvanti = view.findViewById(R.id.buttonAvanti)
        buttonIndietro = view.findViewById(R.id.buttonIndietro)

        // Pulsante indietro generale (esci quiz)
        view.findViewById<View>(R.id.backbutton).setOnClickListener {
            salvaProgressoParziale()
            findNavController().navigate(R.id.action_domandeFragment_to_quizFragment)
        }

        ripristinaProgresso {
            caricaDomande()
        }


        // Avanti: controlla risposta e vai avanti o finisci
        buttonAvanti.setOnClickListener {
            val selectedId = radioGroupOpzioni.checkedRadioButtonId
            if (selectedId != -1) {
                val rispostaSelezionata = when (selectedId) {
                    R.id.opzione1 -> 0
                    R.id.opzione2 -> 1
                    R.id.opzione3 -> 2
                    else -> -1
                }

                // Se risposta corretta, aumenta punteggio
                if (rispostaSelezionata == domande[domandaCorrente].rispostaCorretta) {
                    punteggio++
                }

                domandaCorrente++
                if (domandaCorrente < domande.size) {
                    mostraDomanda()
                } else {
                    mostraRisultato()
                }
            } else {
                Toast.makeText(requireContext(), "Seleziona una risposta!", Toast.LENGTH_SHORT).show()
            }
        }

        // Indietro: torna alla domanda precedente se possibile
        buttonIndietro.setOnClickListener {
            if (domandaCorrente > 0) {
                domandaCorrente--
                mostraDomanda()
            }
        }

        enableTTS()

    }

    private fun caricaDomande() {

        db.collection("quiz_$argomento")
            .get()
            .addOnSuccessListener { result ->

                domande.clear()
                for (document in result) {
                    val domanda = document.toObject(Question::class.java)
                    domande.add(domanda)
                }

                if (domande.isNotEmpty()) {
                    // Se progresso errato, resetta
                    if (domandaCorrente >= domande.size) {
                        domandaCorrente = 0
                        punteggio = 0
                    }
                    mostraDomanda()
                } else {
                    Toast.makeText(requireContext(), "Nessuna domanda trovata", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(requireContext(), "Errore nel caricamento delle domande", Toast.LENGTH_SHORT).show()
            }
    }


    private fun mostraDomanda() {
        if (domande.isEmpty()) {
            return
        }

        if (domandaCorrente >= domande.size) {
            domandaCorrente = 0
        }

        val domanda = domande[domandaCorrente]

        // AGGIORNA L'UI CON I DATI DELLA DOMANDA
        textTitoloDomanda.text = "Domanda ${domandaCorrente + 1}/${domande.size}"
        textDomanda.text = domanda.testo

        // Popola le opzioni di risposta
        opzione1.text = domanda.opzioni[0]
        opzione2.text = domanda.opzioni[1]
        opzione3.text = domanda.opzioni[2]

        // Resetta la selezione del RadioGroup
        radioGroupOpzioni.clearCheck()

        // Gestisci la visibilità dei pulsanti
        buttonIndietro.visibility = if (domandaCorrente > 0) View.VISIBLE else View.GONE
        buttonAvanti.text = if (domandaCorrente < domande.size - 1) "Avanti" else "Termina"

    }

    private fun mostraRisultato() {
        val percentuale = (punteggio.toFloat() / domande.size * 100).toInt()
        val uid = auth.currentUser?.uid ?: return

        db.collection("progressi_utente")
            .document(uid)
            .collection("argomenti")
            .document(argomento)
            .set(mapOf("percentuale" to percentuale))
            .addOnSuccessListener {
                if (percentuale >= 80) assegnaBadge(uid)
                navigateInBaseAlPunteggio()
            }
            .addOnFailureListener {
                navigateInBaseAlPunteggio()
            }
    }

    private fun salvaProgressoParziale() {
        val uid = auth.currentUser?.uid ?: return

        val data = mapOf(
            "domandaCorrente" to domandaCorrente,
            "punteggio" to punteggio
        )

        db.collection("progressi_utente")
            .document(uid)
            .collection("argomenti")
            .document(argomento)
            .set(data, SetOptions.merge())

    }

    private fun ripristinaProgresso(onComplete: () -> Unit) {
        val uid = auth.currentUser?.uid ?: return onComplete()

        db.collection("progressi_utente")
            .document(uid)
            .collection("argomenti")
            .document(argomento)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    domandaCorrente = document.getLong("domandaCorrente")?.toInt() ?: 0
                    punteggio = document.getLong("punteggio")?.toInt() ?: 0
                }
                onComplete()
            }
            .addOnFailureListener {
                onComplete()
            }
    }

    private fun assegnaBadge(uid: String) {
        val badgeMap = mapOf(
            "privacy" to "lock",
            "cybersecurity" to "banned",
            "phishing" to "target",
            "dipendenza" to "eyes",
            "fake" to "fact_check",
            "sicurezza" to "key",
            "truffe" to "private_detective",
            "dati" to "floppy_disk",
            "netiquette" to "earth",
            "navigazione" to  "compass",
        )

        val badgeKey = badgeMap[argomento.lowercase().replace(" ", "_")]

        badgeKey?.let {
            val badgeFieldPath = FieldPath.of("badges", it)
            db.collection("users").document(uid).update(badgeFieldPath, true)
                .addOnSuccessListener {
                    Log.d("DEBUG", "Badge $it correttamente sbloccato")
                }
                .addOnFailureListener { e ->
                    Log.e("DEBUG", "Errore aggiornamento badge: ${e.message}")
                }
        }
    }

    private fun navigateInBaseAlPunteggio() {
        when (punteggio) {
            in 0..3 -> findNavController().navigate(R.id.action_domandeFragment_to_badFragment)
            else -> findNavController().navigate(R.id.action_domandeFragment_to_goodFragment)
        }
    }

    override fun onPause() {
        super.onPause()
        salvaProgressoParziale()
    }
}
