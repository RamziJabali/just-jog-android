package com.eljabali.joggingapplicationandroid.recyclerview

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eljabali.joggingapplicationandroid.R
import com.eljabali.joggingapplicationandroid.mainview.MainActivity
import com.eljabali.joggingapplicationandroid.mapsview.MapsActivity
import com.eljabali.joggingapplicationandroid.recyclerviewmodel.RecyclerViewModel
import com.eljabali.joggingapplicationandroid.recyclerviewmodel.RecyclerViewState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import localdate.extensions.parseLocalDate
import java.lang.RuntimeException

class RecyclerViewFragment : Fragment(), ItemClickListener, RecyclerViewListener {

    private val recyclerViewModel by lazy { RecyclerViewModel() }
    private val recyclerViewAdapter: RecyclerViewAdapter by lazy { RecyclerViewAdapter(this) }
    private val horizontalLayoutManager =
        LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
    private val recyclerView: RecyclerView by lazy {
        requireView().findViewById(R.id.horizontal_recycler_view)
    }

    private val mapButton: Button by lazy { requireView().findViewById(R.id.map_button) }
    private val compositeDisposable = CompositeDisposable()

    companion object {
        const val RVF_TAG = "RecyclerViewFragment"

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

    override fun setRecyclerViewState(recyclerViewState: RecyclerViewState) {

    }

    override fun monitorRecyclerViewState() {
        compositeDisposable.add(
            recyclerViewModel.recyclerViewStateObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { viewState ->
                        setRecyclerViewState(viewState)
                    },
                    { error -> Log.e(RVF_TAG, error.localizedMessage, error) })
        )
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