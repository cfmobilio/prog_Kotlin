package com.example.wa.presentation.profile

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Button
import com.example.wa.MainActivity

object TextSizeHelper {

    fun applyTextSizeToView(context: Context, view: View) {
        val prefs = context.getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE)
        val isLargeText = prefs.getBoolean(MainActivity.KEY_LARGE_TEXT, false)

        if (isLargeText) {
            applyLargeTextRecursively(view)
        }
    }

    private fun applyLargeTextRecursively(view: View) {
        when (view) {
            is TextView -> {
                val currentSize = view.textSize / view.resources.displayMetrics.scaledDensity
                view.textSize = currentSize * 1.25f // Aumenta del 25%
            }
            is Button -> {
                val currentSize = view.textSize / view.resources.displayMetrics.scaledDensity
                view.textSize = currentSize * 1.25f
            }
            is ViewGroup -> {
                // Applica ricorsivamente a tutti i figli
                for (i in 0 until view.childCount) {
                    applyLargeTextRecursively(view.getChildAt(i))
                }
            }
        }
    }
}

// Extension function per semplificare l'uso
fun View.applyAccessibilityTextSize(context: Context) {
    TextSizeHelper.applyTextSizeToView(context, this)
}