package com.example.wa.data.repository

import com.example.wa.R
import com.example.wa.data.model.Emergency

class EmergencyRepository {
    fun getEmergencies(): List<Emergency> {
        return listOf(
            Emergency(
                "Polizia Postale e delle Comunicazioni",
                R.drawable.police,
                "https://www.commissariatodips.it",
                "+39 06 4620 2222"
            ),
            Emergency(
                "Sicurezza Informatica e Attacchi Hacker",
                R.drawable.shield,
                "https://www.cert-agid.gov.it",
                "cert@cert-agid.gov.it"
            ),
            Emergency(
                "Cyberbullismo e Minori Online",
                R.drawable.theatre,
                "https://www.azzurro.it",
                "1.96.96"
            ),
            Emergency(
                "Truffe Online e Carte di Credito",
                R.drawable.credit_card,
                "https://www.consob.it",
                "+39 06 8477 1"
            ),
            Emergency("Centro Antiviolenza", R.drawable.violence, "https://www.1522.eu", "1522"),
            Emergency(
                "Numero Unico Emergenze",
                R.drawable.emergency_call,
                "https://112.gov.it",
                "112"
            ),
            Emergency("Bambini Scomparsi", R.drawable.kid_help, "https://www.116000.it", "116000")
        )
    }
}