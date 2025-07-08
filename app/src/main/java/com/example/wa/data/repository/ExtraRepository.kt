package com.example.wa.data.repository

import com.example.wa.R
import com.example.wa.data.model.App


class ExtraRepository {
    fun getApp(): List<App>{
        return listOf(
            App("Privacy online", "Scopri come proteggere i tuoi dati e la tua identità.", R.drawable.padlock),
            App("Cyberbullismo", "Impara a riconoscere e contrastsre gli attacchi online.", R.drawable.warning),
            App("Phishing", "Riconosci le truffe digitali prima che sia troppo tardi.", R.drawable.mail),
            App("Dipendenza dai social", "Scopri come evitare l'uso eccessivo dei social.", R.drawable.hourglass),
            App("Fake News", "Impara a riconoscere le notizie false e verificare le fonti online.",
                R.drawable.fake),
            App("Sicurezza account", "Impara a proteggere i tuoi account con password sicure.", R.drawable.shield),
            App("Truffe online", "Riconosci le truffe su internet e impara a difenderti.", R.drawable.scam),
            App("Protezione dati", "Proteggi i tuoi dati personali online.", R.drawable.security),
            App("Netiquette", "Rispetta le regole di comportamento online.", R.drawable.netiquette),
            App("Navigazione sicura", "Naviga in sicurezza proteggendo la tua privacy.", R.drawable.secure)
        )
    }

    fun getArgomentiMap(): Map<String, String> {
        return mapOf(
            "Privacy online" to "privacy",
            "Cyberbullismo" to "cyberbullismo",
            "Phishing" to "phishing",
            "Dipendenza dai social" to "dipendenza",
            "Fake News" to "fake",
            "Sicurezza account" to "account",
            "Truffe online" to "truffe",
            "Protezione dati" to "dati",
            "Netiquette" to "netiquette",
            "Navigazione sicura" to "navigazione"
        )
    }

}