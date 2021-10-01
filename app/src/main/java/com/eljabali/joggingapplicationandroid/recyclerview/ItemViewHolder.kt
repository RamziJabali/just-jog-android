package com.eljabali.joggingapplicationandroid.recyclerview

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.eljabali.joggingapplicationandroid.R

class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val jogEntryNumberTextView: TextView = itemView.findViewById(R.id.jog_number)
    private val milePerHourTextView: TextView = itemView.findViewById(R.id.miles_per_hour_value)
    private val totalDistanceTextView: TextView = itemView.findViewById(R.id.total_distance_value)
    private val totalTimeTextView: TextView = itemView.findViewById(R.id.total_time_value)
    private val dateTextView: TextView = itemView.findViewById(R.id.date_value)

    fun bind(recyclerViewProperties: RecyclerViewProperties, onClickListener: ItemClickListener) {
        jogEntryNumberTextView.text = recyclerViewProperties.jogEntry
        milePerHourTextView.text = recyclerViewProperties.milesPerHour
        totalDistanceTextView.text = recyclerViewProperties.totalDistance
        totalTimeTextView.text = recyclerViewProperties.totalTime
        dateTextView.text = recyclerViewProperties.date

        itemView.setOnClickListener {
            onClickListener.onItemClickedListener(recyclerViewProperties.jogEntry.toInt(),recyclerViewProperties.date)
        }
    }
}