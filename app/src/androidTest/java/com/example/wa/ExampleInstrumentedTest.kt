package com.example.wa

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.wa.presentation.profile.TTSHelper
import com.google.firebase.auth.FirebaseAuth
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @Test
    fun useAppContext() {
        val appContext = ApplicationProvider.getApplicationContext<Context>()
        assertEquals("com.example.pro", appContext.packageName)
    }

    @Test
    fun ttsHelper_enableDisableBehavior() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        TTSHelper.init(context.applicationContext as android.app.Application)

        TTSHelper.setEnabled(true)
        assertTrue("TTS dovrebbe essere abilitato", TTSHelper.isEnabled())

        TTSHelper.setEnabled(false)
        assertFalse("TTS dovrebbe essere disabilitato", TTSHelper.isEnabled())
    }

    @Test
    fun testFirebaseAuthInitialization() {
        val auth = FirebaseAuth.getInstance()
        assert(auth != null)
        assert(auth.currentUser == null)
    }
}
