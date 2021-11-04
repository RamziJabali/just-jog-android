package com.eljabali.joggingapplicationandroid.calendar.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eljabali.joggingapplicationandroid.R

class RecyclerViewAdapter(private val itemClickListener: ItemClickListener): RecyclerView.Adapter<ItemViewHolder>() {

    private var recyclerViewProperties: Array<RecyclerViewProperties> = emptyArray()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_holder, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holderItem: ItemViewHolder, position: Int) {
        holderItem.bind(recyclerViewProperties[position], itemClickListener)
    }

    override fun getItemCount(): Int = recyclerViewProperties.size

    fun setRecyclerViewItems(items: List<RecyclerViewProperties>) {
        recyclerViewProperties = items.toTypedArray()
        notifyDataSetChanged()
    }

}