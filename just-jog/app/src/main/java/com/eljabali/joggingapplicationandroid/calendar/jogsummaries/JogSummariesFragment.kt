package com.eljabali.joggingapplicationandroid.calendar.jogsummaries

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eljabali.joggingapplicationandroid.R
import com.eljabali.joggingapplicationandroid.map.view.MapsActivity
import io.reactivex.disposables.CompositeDisposable
import localdate.extensions.parseLocalDate

class JogSummariesFragment : Fragment(), JogClickListener {

    companion object {
        fun newInstance() = JogSummariesFragment()
    }

    private val jogSummariesAdapter: JogSummariesAdapter by lazy { JogSummariesAdapter(this) }
    private val recyclerView: RecyclerView by lazy {
        requireView().findViewById(R.id.horizontal_recycler_view)
    }
    private val compositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_jog_summaries, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = jogSummariesAdapter
        }
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.clear()
    }

    override fun onJogClickedListener(jogId: Int, stringDate: String) {
        requireContext().startActivity(
            MapsActivity.newInstance(
                requireActivity().applicationContext,
                runID = jogId,
                localDate = stringDate.parseLocalDate() ?: throw RuntimeException()
            )
        )
    }

    fun updateListOfProperties(listOfProperties: List<JogSummaryProperties>) {
        jogSummariesAdapter.setJogSummaries(listOfProperties)
    }
}