package com.eljabali.joggingapplicationandroid.calendar.jogsummaries

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.eljabali.joggingapplicationandroid.R

class JogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val jogEntryNumberTextView: TextView = itemView.findViewById(R.id.jog_number)
    private val milePerHourTextView: TextView = itemView.findViewById(R.id.miles_per_hour_value)
    private val totalDistanceTextView: TextView = itemView.findViewById(R.id.total_distance_value)
    private val totalTimeTextView: TextView = itemView.findViewById(R.id.total_time_value)
    private val dateTextView: TextView = itemView.findViewById(R.id.date_value)
    private val jogSummaryId: TextView = itemView.findViewById(R.id.jog_summary_id)
    private val jogStartTime: TextView = itemView.findViewById(R.id.start_time_value)
    private val mapsButton: Button = itemView.findViewById(R.id.map_button)
    fun bind(jogSummaryProperties: JogSummaryProperties, onClickListener: JogClickListener) {
        jogEntryNumberTextView.text = jogSummaryProperties.jogEntryCountOfDay
        milePerHourTextView.text = jogSummaryProperties.milesPerHour
        totalDistanceTextView.text = jogSummaryProperties.totalDistance
        totalTimeTextView.text = jogSummaryProperties.totalTime
        dateTextView.text = jogSummaryProperties.date
        jogSummaryId.text = jogSummaryProperties.jogSummaryId
        jogStartTime.text = jogSummaryProperties.startTime
        mapsButton.setOnClickListener {
            onClickListener.onJogClickedListener(
                jogSummaryProperties.jogSummaryId.toInt(),
                jogSummaryProperties.date
            )
        }
    }
}