package com.eljabali.joggingapplicationandroid.statisticsview

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.eljabali.joggingapplicationandroid.R
import com.eljabali.joggingapplicationandroid.statisticsviewmodel.StatisticsViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [JogStatisticsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class JogStatisticsFragment : Fragment(), ViewListener {
    // TODO: Rename and change types of parameters
//    private var param1: String? = null
//    private var param2: String? = null
//    arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
//    }
    private val statisticsViewModel: StatisticsViewModel by lazy { StatisticsViewModel() }
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    private val dateTextView: TextView by lazy { requireView().findViewById(R.id.date) }
    private val timeTextView: TextView by lazy { requireView().findViewById(R.id.time) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        statisticsViewModel.onFragmentLaunch()
        onLaunch()
        monitorStatisticsViewState()
    }

    fun onLaunch() {
        statisticsViewModel.onFragmentLaunch()
    }

    override fun monitorStatisticsViewState() {
        compositeDisposable.add(
            statisticsViewModel.observableStatisticsViewState
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { statisticsViewState -> setNewViewState(statisticsViewState) },
                    { error -> Log.e("Error", error.localizedMessage, error) }
                )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_jog_statistics, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment JogStatisticsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            JogStatisticsFragment()
//                .apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
    }

    override fun setNewViewState(statisticsViewState: StatisticsViewState) {
        dateTextView.text = statisticsViewState.date
        timeTextView.text = statisticsViewState.time
    }
}