package com.junyoung.day

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.ProgressDialog

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer

import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide

import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.OnProgressListener
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask

import kotlinx.android.synthetic.main.activity_person_fragment_tab.*
import java.io.IOException

import java.text.SimpleDateFormat
import java.time.*
import java.util.*


class PersonFragmentTab : Fragment() {
    lateinit var callbackMethod: OnDateSetListener
    lateinit var callbackMethod2: OnDateSetListener
    private lateinit var mContext: Context
    var filename: String? = null
    private var mStorageRef: StorageReference? = null
    private var filePath: Uri? = null
    val user = FirebaseAuth.getInstance().currentUser
    var ddaystart: Calendar = Calendar.getInstance()
    var ddayend: Calendar = Calendar.getInstance()
    var ddaynoew = Calendar.getInstance()
    val storage: FirebaseStorage =
        FirebaseStorage.getInstance("gs://military-service-54f50.appspot.com")
    val storageRef: StorageReference = storage.getReference()
    val pathReference: StorageReference = storageRef.child("images")

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

        btn_saveimage.setOnClickListener {
            val intent = Intent()
            intent.setType("image/*")
            intent.setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(Intent.createChooser(intent, "이미지를 선택하세요"), 0)
        }
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


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //request 코드가 0이고 ok를 선택했고 data에 뭔가가 들어있다면
        if (requestCode == 0 && resultCode == AppCompatActivity.RESULT_OK) {
            filePath = data?.getData()
            try {
                uploadfile()


            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
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
        val myname: String = edit_myname.text.toString()
        startActivity(Intent(activity, Home::class.java))


        if (left <= 0) {
            text_progressbar.setText(progress.toString())
            text_total_serve.setText("" + total)
            text_has_serve.setText("" + has)
            text_left_serve.setText("" + left)
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
            text_total_serve.setText("" + total)
            text_has_serve.setText("" + has)
            text_left_serve.setText("" + left)
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

    fun uploadfile() {

        //업로드할 파일이 있으면 수행

        if (filePath != null) {
            //업로드 진행 Dialog 보이기
            val progressDialog: ProgressDialog = ProgressDialog(activity)
            progressDialog.setTitle("업로드중...")
            progressDialog.show();

            //storage
            val storage: FirebaseStorage = FirebaseStorage.getInstance()

            //Unique한 파일명을 만들자.
            val formatter: SimpleDateFormat = SimpleDateFormat("yyyyMMHH_mmss")
            val now: Date = Date()

            filename = user!!.uid + ".png"
            //storage 주소와 폴더 파일명을 지정해 준다.
            mStorageRef = storage.getReferenceFromUrl("gs://military-service-54f50.appspot.com")
                .child("images/" + filename)
            //올라가거라...
            mStorageRef!!.putFile(filePath!!)
                //성공시
                .addOnSuccessListener(OnSuccessListener<UploadTask.TaskSnapshot>() {
                    progressDialog.dismiss(); //업로드 진행 Dialog 상자 닫기
                    Toast.makeText(activity, "업로드 완료!", Toast.LENGTH_SHORT).show()
                    val adddata = ImageData(imagedata = filename)
                    viewModel.addImageData(adddata)
                    val intent = Intent(activity, Home::class.java)

                    startActivity(intent)


                })
                //실패시
                .addOnFailureListener(OnFailureListener() {
                    progressDialog.dismiss()
                    Toast.makeText(
                        activity,
                        "업로드 실패!" + filePath + "ggg" + mStorageRef,
                        Toast.LENGTH_SHORT
                    ).show()

                })
                //진행중
                .addOnProgressListener(OnProgressListener<UploadTask.TaskSnapshot>() {


                })
        } else {
            Toast.makeText(activity, "업로드 완료!", Toast.LENGTH_SHORT).show()


        }


    }
}
