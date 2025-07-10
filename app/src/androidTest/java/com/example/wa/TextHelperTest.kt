package com.example.wa

import android.content.Context
import android.widget.TextView
import android.widget.LinearLayout
import androidx.test.core.app.ApplicationProvider
import com.example.wa.presentation.profile.TextSizeHelper
import org.junit.Test
import org.junit.Assert.assertTrue

class TextSizeHelperTest {

    private val context = ApplicationProvider.getApplicationContext<Context>()

    @Test
    fun testApplyLargeTextToTextView() {
        val textView = TextView(context).apply {
            textSize = 16f
        }

        TextSizeHelper.applyLargeTextRecursively(textView)

        assertTrue("textSize should be > 16f", textView.textSize > 16f)
    }

    @Test
    fun testApplyLargeTextToViewGroup() {
        val parent = LinearLayout(context)
        val child1 = TextView(context).apply { textSize = 14f }
        val child2 = TextView(context).apply { textSize = 14f }

        parent.addView(child1)
        parent.addView(child2)

        TextSizeHelper.applyLargeTextRecursively(parent)

        assertTrue("child1.textSize should be > 14f", child1.textSize > 14f)
        assertTrue("child2.textSize should be > 14f", child2.textSize > 14f)
    }
}
