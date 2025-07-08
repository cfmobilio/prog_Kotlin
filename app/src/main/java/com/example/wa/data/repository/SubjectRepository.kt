package com.example.wa.data.repository

import com.example.wa.data.model.Subject
import com.example.wa.R

class SubjectRepository {
    fun getSubjects(): List<Subject> {
        return listOf(
            Subject("Privacy online", R.drawable.padlock),
            Subject("Cyberbullismo", R.drawable.warning),
            Subject("Phishing", R.drawable.mail),
            Subject("Dipendenza dai social", R.drawable.hourglass),
            Subject("Fake News", R.drawable.fake),
            Subject("Sicurezza account", R.drawable.shield),
            Subject("Truffe online", R.drawable.scam),
            Subject("Protezione dati", R.drawable.security),
            Subject("Netiquette", R.drawable.netiquette),
            Subject("Navigazione sicura", R.drawable.secure)
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