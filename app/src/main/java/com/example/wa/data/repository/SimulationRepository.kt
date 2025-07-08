package com.example.wa.data.repository

import com.example.wa.R
import com.example.wa.data.model.Argomenti


class SimulationRepository {

    fun getArg(): List<Argomenti>{
        return listOf(
            Argomenti("Privacy online", "Entra nella simulazione!", R.drawable.padlock),
            Argomenti("Cyberbullismo", "Entra nella simulazione!", R.drawable.warning),
            Argomenti("Phishing", "Entra nella simulazione!", R.drawable.mail),
            Argomenti("Dipendenza dai social", "Entra nella simulazione!", R.drawable.hourglass),
            Argomenti("Fake News", "Entra nella simulazione!", R.drawable.fake),
            Argomenti("Sicurezza account", "Entra nella simulazione!", R.drawable.shield),
            Argomenti("Truffe online", "Entra nella simulazione!", R.drawable.scam),
            Argomenti("Protezione dati", "Entra nella simulazione!", R.drawable.security),
            Argomenti("Netiquette", "Entra nella simulazione!", R.drawable.netiquette),
            Argomenti("Navigazione sicura", "Entra nella simulazione!", R.drawable.secure)
        )
    }

    fun getArgomentiMap() : Map<String,String>{
       return mapOf(
            "Privacy online" to "gJgWCSPa5MBYXMfQmtc1",
            "Cyberbullismo" to "lH30PTcSfjL0vIiEr3er\n",
            "Phishing" to "jxwPIteFkJ9orjWyzUqc",
            "Dipendenza dai social" to "44XbIlDC6QgnyaSDUymb",
            "Fake News" to "K0HClxqdyaIxKXJrnPxQ",
            "Sicurezza account" to "qOQ56qVTaAZbMZfSf3gV",
            "Truffe online" to "eXrpE2Hb9dje0iGJ8Fcy",
            "Protezione dati" to "rUIsjgd6V31dMqhA3F2U",
            "Netiquette" to "dUGV5FVdBCIoOJGwy01M",
            "Navigazione sicura" to "2BJYJOwJyjkkgSupWVCf"
       )
    }

}