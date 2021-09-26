package com.eljabali.joggingapplicationandroid.recyclerview

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.eljabali.joggingapplicationandroid.R

class ItemViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
    val jogEntryNumberTextView: TextView = itemView.findViewById(R.id.jog_number)
    val milePerHourTextView: TextView = itemView.findViewById(R.id.miles_per_hour_value)
    val totalDistanceTextView: TextView = itemView.findViewById(R.id.total_distance_value)
    val totalTimeTextView: TextView = itemView.findViewById(R.id.total_time_value)
}