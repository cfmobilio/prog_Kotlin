package com.example.wa.presentation.quiz

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.wa.R
import com.example.wa.data.model.Quiz

class QuizAdapter(
    private var lista: MutableList<Quiz>,
    private val onItemClick: (Quiz) -> Unit
) : RecyclerView.Adapter<QuizAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titolo: TextView = view.findViewById(R.id.titoloTema)
        val icona: ImageView = view.findViewById(R.id.emergencyIcon)
        val progresso: ProgressBar = view.findViewById(R.id.progressoTema)
        val percentuale: TextView = view.findViewById(R.id.percentualeTema)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_quiz, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val argomento = lista[position]

        holder.titolo.text = argomento.titolo
        holder.icona.setImageResource(argomento.icona)
        holder.icona.contentDescription = "Icona per l'argomento ${argomento.titolo}"

        val progressoPercentuale = argomento.percentuale.coerceIn(0, 100)
        holder.progresso.progress = progressoPercentuale
        holder.percentuale.text = "$progressoPercentuale%"

        holder.itemView.setOnClickListener {
            onItemClick(argomento)
        }
    }

    override fun getItemCount(): Int = lista.size

    fun updateQuizList(newList: List<Quiz>) {
        lista.clear()
        lista.addAll(newList)
        notifyDataSetChanged()
    }

    fun aggiornaProgresso(argomentoKey: String, nuovaPercentuale: Int) {
        val index = lista.indexOfFirst { it.argomentoKey == argomentoKey }
        if (index != -1) {
            lista[index] = lista[index].copy(percentuale = nuovaPercentuale.coerceIn(0, 100))
            notifyItemChanged(index)
        }
    }
}
