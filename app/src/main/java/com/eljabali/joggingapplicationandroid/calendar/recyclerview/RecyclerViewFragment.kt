package com.eljabali.joggingapplicationandroid.calendar.recyclerview

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eljabali.joggingapplicationandroid.R
import com.eljabali.joggingapplicationandroid.calendar.recyclerviewmodel.RecyclerViewModel
import com.eljabali.joggingapplicationandroid.calendar.recyclerviewmodel.RecyclerViewState
import com.eljabali.joggingapplicationandroid.map.view.MapsActivity
import com.eljabali.joggingapplicationandroid.util.TAG
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import localdate.extensions.parseLocalDate

class RecyclerViewFragment : Fragment(), ItemClickListener, RecyclerViewListener {

    companion object {
        @JvmStatic
        fun newInstance() = RecyclerViewFragment()
    }

    private val recyclerViewModel by lazy { RecyclerViewModel() }
    private val recyclerViewAdapter: RecyclerViewAdapter by lazy { RecyclerViewAdapter(this) }
    private val horizontalLayoutManager =
        LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

    private val recyclerView: RecyclerView by lazy {
        requireView().findViewById(R.id.horizontal_recycler_view)
    }

    private val compositeDisposable = CompositeDisposable()

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

    override fun setRecyclerViewState(recyclerViewState: RecyclerViewState) {

    }

    override fun monitorRecyclerViewState() {
        recyclerViewModel.recyclerViewStateObservable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { viewState ->
                    setRecyclerViewState(viewState)
                },
                { error -> Log.e(TAG, error.localizedMessage, error) })
            .addTo(compositeDisposable)
    }

    fun updateListOfProperties(listOfProperties: List<RecyclerViewProperties>) {
        recyclerViewAdapter.setRecyclerViewItems(listOfProperties)
    }

    override fun onItemClickedListener(runID: Int, stringDate: String) {
        requireContext().startActivity(
            MapsActivity.newInstance(
                requireActivity().applicationContext,
                runID = runID,
                localDate = stringDate.parseLocalDate() ?: throw RuntimeException()
            )
        )
    }
}