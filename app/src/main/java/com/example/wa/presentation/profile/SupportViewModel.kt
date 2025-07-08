package com.example.wa.presentation.profile

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.ViewModel

class SupportViewModel : ViewModel() {

    fun sendEmail(context: Context) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:supporto@example.com")
            putExtra(Intent.EXTRA_SUBJECT, "Richiesta di Supporto")
            putExtra(Intent.EXTRA_TEXT, "Ciao, ho bisogno di aiuto con...")
        }

        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(Intent.createChooser(intent, "Invia email con:"))
        } else {
            Toast.makeText(context, "Nessuna app email trovata", Toast.LENGTH_SHORT).show()
        }
    }

    fun validateAndSubmitFeedback(
        context: Context,
        rating: Int,
        feedback: String,
        onSuccess: () -> Unit
    ) {
        if (rating == 0) {
            Toast.makeText(context, "Per favore valuta l'app con le stelle", Toast.LENGTH_SHORT).show()
            return
        }

        if (feedback.isBlank()) {
            Toast.makeText(context, "Scrivi un commento prima di inviare", Toast.LENGTH_SHORT).show()
            return
        }

        Toast.makeText(context, "Grazie per il tuo feedback!", Toast.LENGTH_LONG).show()
        onSuccess()
    }
}
