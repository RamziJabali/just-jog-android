package com.eljabali.joggingapplicationandroid.calendar.jogsummaries

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eljabali.joggingapplicationandroid.R

class JogSummariesAdapter(private val jogClickListener: JogClickListener): RecyclerView.Adapter<JogViewHolder>() {

    private var jogSummaryProperties: Array<JogSummaryProperties> = emptyArray()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JogViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_holder, parent, false)
        return JogViewHolder(view)
    }

    override fun onBindViewHolder(holderJog: JogViewHolder, position: Int) {
        holderJog.bind(jogSummaryProperties[position], jogClickListener)
    }

    override fun getItemCount(): Int = jogSummaryProperties.size

    fun setJogSummaries(items: List<JogSummaryProperties>) {
        jogSummaryProperties = items.toTypedArray()
        notifyDataSetChanged()
    }

}