package com.eljabali.joggingapplicationandroid.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eljabali.joggingapplicationandroid.R

class RecyclerViewAdapter : RecyclerView.Adapter<ItemViewHolder>() {

    private var recyclerViewProperties: Array<RecyclerViewProperties> = emptyArray()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_holder, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holderItem: ItemViewHolder, position: Int) {
        holderItem.jogEntryNumberTextView.text = recyclerViewProperties[position].jogEntry
        holderItem.milePerHourTextView.text = recyclerViewProperties[position].milesPerHour
        holderItem.totalDistanceTextView.text = recyclerViewProperties[position].totalDistance
        holderItem.totalTimeTextView.text = recyclerViewProperties[position].totalTime
    }

    override fun getItemCount(): Int = recyclerViewProperties.size

    fun setRecyclerViewItems(items: List<RecyclerViewProperties>) {
        recyclerViewProperties = items.toTypedArray()
        notifyDataSetChanged()
    }
}