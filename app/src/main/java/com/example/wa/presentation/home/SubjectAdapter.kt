package com.example.wa.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.wa.R
import com.example.wa.data.model.Subject

class SubjectAdapter(
    private var lista: List<Subject>,
    private var argomentiMap: Map<String, String> = emptyMap()
) : RecyclerView.Adapter<SubjectAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titolo: TextView = view.findViewById(R.id.cardTitle)
        val icona: ImageView = view.findViewById(R.id.cardIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val argomento = lista[position]
        holder.titolo.text = argomento.titolo
        holder.icona.setImageResource(argomento.icona)

        // Aggiungi una descrizione per le immagini (accessibilità)
        holder.icona.contentDescription = "Icona per l'argomento ${argomento.titolo}"

        // Gestisci il click sull'elemento
        holder.itemView.setOnClickListener {
            val key = argomentiMap[argomento.titolo] ?: "altro"

            val bundle = Bundle().apply {
                putString("argomento", key)
            }

            it.findNavController().navigate(R.id.action_homeFragment_to_infoFragment, bundle)
        }
    }

    override fun getItemCount(): Int = lista.size

    fun updateSubjects(newList: List<Subject>) {
        lista = newList
        notifyDataSetChanged()
    }

    fun updateArgomentiMap(newMap: Map<String, String>) {
        argomentiMap = newMap
        notifyDataSetChanged()
    }
}