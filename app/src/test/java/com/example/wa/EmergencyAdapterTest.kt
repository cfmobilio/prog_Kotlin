package com.example.wa

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.test.core.app.ApplicationProvider
import com.example.wa.data.model.Emergency
import com.example.wa.presentation.emergency.EmergencyAdapter
import io.mockk.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28])
class EmergencyAdapterTest {

    private lateinit var adapter: EmergencyAdapter
    private lateinit var context: Context
    private val mockEmergencies = listOf(
        Emergency(
            titolo = "Polizia Postale",
            icona = R.drawable.police,
            sito = "https://www.poliziadistato.it",
            contatto = "113"
        ),
        Emergency(
            titolo = "Emergenza Email",
            icona = R.drawable.mail,
            sito = "https://www.emergenza.it",
            contatto = "help@emergenza.it"
        )
    )

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        adapter = EmergencyAdapter(mockEmergencies)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `adapter should have correct item count`() {
        assertEquals(2, adapter.itemCount)
    }

    @Test
    fun `adapter should have zero item count when list is empty`() {
        val emptyAdapter = EmergencyAdapter(emptyList())
        assertEquals(0, emptyAdapter.itemCount)
    }

    @Test
    fun `updateEmergencies should update the list and notify changes`() {
        // Given
        val newEmergencies = listOf(
            Emergency(
                titolo = "Nuova Emergenza",
                icona = R.drawable.emergency_call,
                sito = "https://www.nuova.it",
                contatto = "555-1234"
            )
        )

        // When
        adapter.updateEmergencies(newEmergencies)

        // Then
        assertEquals(1, adapter.itemCount)
    }

    @Test
    fun `onCreateViewHolder should create ViewHolder with correct layout`() {
        // Given
        val parent = mockk<android.view.ViewGroup>()
        val mockView = mockk<View>(relaxed = true)
        val mockInflater = mockk<LayoutInflater>()

        every { parent.context } returns context
        mockkStatic(LayoutInflater::class)
        every { LayoutInflater.from(context) } returns mockInflater
        every { mockInflater.inflate(R.layout.item_emergency, parent, false) } returns mockView
        every { mockView.findViewById<TextView>(R.id.emergencyTitle) } returns mockk()
        every { mockView.findViewById<ImageView>(R.id.emergencyIcon) } returns mockk()
        every { mockView.findViewById<TextView>(R.id.emergencySite) } returns mockk()
        every { mockView.findViewById<TextView>(R.id.emergencyContact) } returns mockk()

        // When
        val viewHolder = adapter.onCreateViewHolder(parent, 0)

        // Then
        assertNotNull(viewHolder)
        assertNotNull(viewHolder.titolo)
        assertNotNull(viewHolder.icona)
        assertNotNull(viewHolder.sito)
        assertNotNull(viewHolder.contatto)
    }

    @Test
    fun `ViewHolder should initialize all views correctly`() {
        // Given
        val mockView = mockk<View>(relaxed = true)
        val mockTitolo = mockk<TextView>()
        val mockIcona = mockk<ImageView>()
        val mockSito = mockk<TextView>()
        val mockContatto = mockk<TextView>()

        every { mockView.findViewById<TextView>(R.id.emergencyTitle) } returns mockTitolo
        every { mockView.findViewById<ImageView>(R.id.emergencyIcon) } returns mockIcona
        every { mockView.findViewById<TextView>(R.id.emergencySite) } returns mockSito
        every { mockView.findViewById<TextView>(R.id.emergencyContact) } returns mockContatto

        // When
        val viewHolder = EmergencyAdapter.ViewHolder(mockView)

        // Then
        assertEquals(mockTitolo, viewHolder.titolo)
        assertEquals(mockIcona, viewHolder.icona)
        assertEquals(mockSito, viewHolder.sito)
        assertEquals(mockContatto, viewHolder.contatto)
    }
}