package com.junyoung.day

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils.isEmpty
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import android.widget.DatePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_person_fragment_tab.*
import java.lang.Math.ceil
import java.lang.Math.floor
import java.time.*
import java.util.*
import java.util.Calendar.*

class PersonFragmentTab : Fragment() {
    lateinit var callbackMethod: OnDateSetListener
    lateinit var callbackMethod2: OnDateSetListener
    private lateinit var mContext: Context
    var ddaystart: Calendar = Calendar.getInstance()
    var ddayend: Calendar = Calendar.getInstance()
    var ddaynoew = Calendar.getInstance()

    @RequiresApi(Build.VERSION_CODES.O)
    val ddayyear: Int = LocalDate.now().year

    @RequiresApi(Build.VERSION_CODES.O)
    val ddaymonth: Int = LocalDate.now().monthValue

    @RequiresApi(Build.VERSION_CODES.O)
    val ddayday: Int = LocalDate.now().dayOfMonth

    private val viewModel: DayViewModel by viewModels()


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        ddaynoew.set(ddayyear, ddaymonth, ddayday)

        return inflater.inflate(R.layout.activity_person_fragment_tab, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //시작날짜
        btn_start.setOnClickListener {
            onClickstart()
        }
        //전역날짜
        btn_end.setOnClickListener {

            onClickend()
        }
        //날짜 보여주기
        viewModel.Livedata.observe(viewLifecycleOwner, Observer {
            text_total_serve.setText(viewModel.Livedata.value!![0].totalserve)
            text_has_serve.setText(viewModel.Livedata.value!![0].hasserve)
            text_left_serve.setText(viewModel.Livedata.value!![0].leftserve)
            text_progressbar.setText(viewModel.Livedata.value!![0].progressbar)
            text_nickname.setText(viewModel.Livedata.value!![0].nickname)
            progressBar.setProgress(viewModel.Livedata.value!![0].progressbar!!.toInt())
            text_myname.setText(viewModel.Livedata.value!![0].myname)



        })
        btn_aaa.setOnClickListener {
            showday()
        }
        viewModel.getDataCalender()






    }

    fun onClickstart() {
        mContext = requireContext()
        callbackMethod = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            btn_start.setText("" + year + "" + (month + 1) + "" + dayOfMonth)
            ddaystart.set(year, month + 1, dayOfMonth)
        }
        val dialog: DatePickerDialog = DatePickerDialog(mContext, callbackMethod, 2018, 9, 2)
        dialog.show()
    }

    fun onClickend() {
        mContext = requireContext()
        callbackMethod2 = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            btn_end.setText("" + year + "" + (month + 1) + "" + dayOfMonth)
            ddayend.set(year, month + 1, dayOfMonth)
        }
        val dialog: DatePickerDialog = DatePickerDialog(
            mContext, callbackMethod2, 2020, 4,
            16
        )
        dialog.show()

    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun showday() {
        val total: Long = countday()
        val has: Long = HasServeday()
        val left: Long = countday() - HasServeday()
        val aaa: Double = (has.toDouble() / total.toDouble()) * 100.0
        val progress: Long = aaa.toLong()
        val myname : String = edit_myname.text.toString()



        if (left <= 0) {
            text_progressbar.setText(progress.toString())
            text_total_serve.setText(""+total)
            text_has_serve.setText(""+has)
            text_left_serve.setText(""+left)
            text_nickname.setText("민간인")
            progressBar.setProgress(100)
            text_myname.setText(myname)
            val addCal = FireData(
                totalserve = total.toString(),
                hasserve = has.toString(),
                leftserve = left.toString(),
                nickname = "민간인",
                progressbar = "100",
                myname = myname
            )
            viewModel.addDataCalender(addCal)

        } else if (left > 0) {
            progressBar.setProgress(progress.toInt())
            text_progressbar.setText(progress.toString())
            text_total_serve.setText(""+total)
            text_has_serve.setText(""+has)
            text_left_serve.setText(""+left)
            text_nickname.setText("군인")
            text_myname.setText(myname)

            val addCal = FireData(
                totalserve = total.toString(),
                hasserve = has.toString(),
                leftserve = left.toString(),
                nickname = "군인",
                progressbar = progress.toString(),
                myname = myname
            )
            viewModel.addDataCalender(addCal)
            //저장하기 클릭후 홈화면으로 이동.
            startActivity(Intent(activity,Home::class.java))


        }





    }

    fun countday(): Long {
        val daystart: Long = ddaystart.timeInMillis
        val dayend: Long = ddayend.timeInMillis

        return ((dayend - daystart) / 86400000) - 1
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun HasServeday(): Long {
        val daystart: Long = ddaystart.timeInMillis

        val daynow = ddaynoew.timeInMillis
        return ((daynow - daystart) / 86400000) - 1
    }


}
