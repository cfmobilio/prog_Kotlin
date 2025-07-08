package com.example.wa.presentation.insight

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.wa.R
import com.example.wa.data.model.App

class ExtraAdapter(
    private var argomentiMap: Map<String, String> = emptyMap()
) : RecyclerView.Adapter<ExtraAdapter.ViewHolder>() {

    private var apps: List<App> = emptyList()

    fun updateApps(newApps: List<App>) {
        apps = newApps
        notifyDataSetChanged()
    }

    fun updateArgomentiMap(newMap: Map<String, String>) {
        argomentiMap = newMap
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titolo: TextView = view.findViewById(R.id.extraTitle)
        val sottotitolo: TextView=view.findViewById(R.id.extraSubtitle)
        val icona: ImageView = view.findViewById(R.id.extraImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_extra, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val argomento = apps[position]
        holder.titolo.text = argomento.titolo
        holder.sottotitolo.text= argomento.sottotitolo
        holder.icona.setImageResource(argomento.icona)
        holder.icona.contentDescription = "Icona per l'argomento ${argomento.titolo}"

        holder.itemView.setOnClickListener {
            val key = argomentiMap[argomento.titolo] ?: "altro"
            val bundle = Bundle().apply {
                putString("argomento", key)
            }
            it.findNavController().navigate(R.id.action_extraFragment_to_insightFragment, bundle)
        }
    }

    override fun getItemCount(): Int = apps.size
}
