package com.example.wa.presentation.simulation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.wa.R
import com.example.wa.data.model.Argomenti

class SimulationAdapter(
    private var argomentiMap: Map<String, String> = emptyMap()
) : RecyclerView.Adapter<SimulationAdapter.ViewHolder>() {

    private var args: List<Argomenti> = emptyList()

    fun updateApps(newArgs: List<Argomenti>) {
        args = newArgs
        notifyDataSetChanged()
    }

    fun updateArgomentiMap(newMap: Map<String, String>) {
        argomentiMap = newMap
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titolo: TextView = view.findViewById(R.id.titolo)
        val sottotitolo: TextView = view.findViewById(R.id.sottotitolo)
        val icona: ImageView = view.findViewById(R.id.icona)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_simulation, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val argomento = args[position]
        holder.titolo.text = argomento.titolo
        holder.sottotitolo.text = argomento.sottotitolo
        holder.icona.setImageResource(argomento.icona)
        holder.icona.contentDescription = "Icona per l'argomento ${argomento.titolo}"

        holder.itemView.setOnClickListener {
            val key = argomentiMap[argomento.titolo] ?: "altro"
            val bundle = Bundle().apply {
                putString("argomento", key)
            }
            it.findNavController().navigate(R.id.action_simulationFragment_to_situationFragment, bundle)
        }
    }

    override fun getItemCount(): Int = args.size

}