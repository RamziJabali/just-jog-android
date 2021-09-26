package com.eljabali.joggingapplicationandroid.recyclerview

import android.graphics.drawable.ClipDrawable.HORIZONTAL
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eljabali.joggingapplicationandroid.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

class RecyclerViewFragment : Fragment() {

    private val recyclerViewAdapter: RecyclerViewAdapter by lazy { RecyclerViewAdapter() }
    private val horizontalLayoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL, false)
    private val recyclerView: RecyclerView by lazy {
        requireView().findViewById(R.id.horizontal_recycler_view)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            RecyclerViewFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_recycler_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<RecyclerView>(R.id.horizontal_recycler_view).apply {
            layoutManager = horizontalLayoutManager
        }
        recyclerView.adapter = recyclerViewAdapter
    }

    fun updateListOfProperties(listOfProperties: List<RecyclerViewProperties>){
        recyclerViewAdapter.setRecyclerViewItems(listOfProperties)
    }

}