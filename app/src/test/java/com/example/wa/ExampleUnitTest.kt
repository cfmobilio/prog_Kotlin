package com.example.wa

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.wa.presentation.home.HomeViewModel
import com.example.wa.presentation.profile.TTSHelper
import com.example.wa.data.model.QuizProgress
import com.example.wa.data.repository.QuizRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import java.util.*

class UnitTests {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun homeViewModel_initializesSubjectsAndMap() {
        val viewModel = HomeViewModel()
        val subjects = viewModel.subjects.value
        val map = viewModel.argomentiMap.value

        assertNotNull("Subjects non dovrebbe essere null", subjects)
        assertTrue("Ci si aspetta almeno un subject", !subjects.isNullOrEmpty())
        assertNotNull("Argomenti map non dovrebbe essere null", map)
        assertTrue("La mappa degli argomenti deve contenere valori", !map.isNullOrEmpty())
    }

    @Test
    fun quizProgress_defaultValuesCorrect() {
        val progress = QuizProgress("phishing")
        assertEquals(0, progress.domandaCorrente)
        assertEquals(0, progress.punteggio)
        assertEquals(0, progress.percentuale)
        assertEquals("phishing", progress.argomento)
    }
}
