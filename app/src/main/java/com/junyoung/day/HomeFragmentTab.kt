package com.junyoung.day


import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import kotlinx.android.synthetic.main.activity_home_fragment_tab.*


import java.util.*


class HomeFragmentTab : Fragment() {
    private val viewModel: DayViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel.getDataCalender()
        return inflater.inflate(R.layout.activity_home_fragment_tab, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.Livedata.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            text_total_serve2.setText(viewModel.Livedata.value!![0].totalserve)
            text_has_serve2.setText(viewModel.Livedata.value!![0].hasserve)
            text_left_serve2.setText(viewModel.Livedata.value!![0].leftserve)
            text_progressbar2.setText(viewModel.Livedata.value!![0].progressbar)
            text_nickname2.setText(viewModel.Livedata.value!![0].nickname)
            progressBar2.setProgress(viewModel.Livedata.value!![0].progressbar!!.toInt())
            text_myname2.setText(viewModel.Livedata.value!![0].myname)

        })

    }


}
