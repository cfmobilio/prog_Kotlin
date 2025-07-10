package com.example.wa.presentation.emergency

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.wa.R
import com.example.wa.data.model.Emergency
import androidx.core.net.toUri

class EmergencyAdapter(private var lista: List<Emergency>) :
    RecyclerView.Adapter<EmergencyAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titolo: TextView = view.findViewById(R.id.emergencyTitle)
        val icona: ImageView = view.findViewById(R.id.emergencyIcon)
        val sito: TextView = view.findViewById(R.id.emergencySite)
        val contatto: TextView = view.findViewById(R.id.emergencyContact)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_emergency, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val emergency = lista[position]

        holder.titolo.text = emergency.titolo
        holder.icona.setImageResource(emergency.icona)
        holder.sito.text = emergency.sito
        holder.contatto.text = emergency.contatto

        holder.sito.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, emergency.sito.toUri())
            holder.itemView.context.startActivity(intent)
        }

        holder.contatto.setOnClickListener {
            if (emergency.contatto.contains("@")) {

                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = "mailto:${emergency.contatto}".toUri()
                }
                holder.itemView.context.startActivity(intent)
            } else {
                val intent = Intent(Intent.ACTION_DIAL).apply {
                    data = "tel:${emergency.contatto}".toUri()
                }
                holder.itemView.context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int = lista.size

    fun updateEmergencies(newList: List<Emergency>) {
        lista = newList
        notifyDataSetChanged()
    }
}