package com.example.wa.fcm

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.wa.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    companion object {
        private const val CHANNEL_ID = "webaware_channel"
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // titolo / testo dal blocco "notification" o "data"
        val title = remoteMessage.notification?.title ?: remoteMessage.data["title"]
        val body  = remoteMessage.notification?.body  ?: remoteMessage.data["body"]

        // Intent che apre MainActivity (o un deep‑link al fragment che vuoi)
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // canale (una sola volta, ma ricrearlo è innocuo)
        createNotificationChannel()

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(System.currentTimeMillis().toInt(), notification)
    }

    // crea il canale solo su Android 8+
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "WebAware notifiche",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    // facoltativo ma utile: logga / invia token al tuo backend
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // TODO: invia il token al tuo server se ti serve
    }
}
