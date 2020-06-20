package com.junyoung.day

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.OnProgressListener
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_add_post.*
import java.io.IOException
import androidx.fragment.app.viewModels
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*

class AddPost : AppCompatActivity() {
    private var filePath: Uri? = null
    private var mStorageRef: StorageReference? = null
    private  val viewModel : DayViewModel by viewModels()

    var filename :String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post)


        btn_gallary.setOnClickListener {
            val intent = Intent()
            intent.setType("image/*")
            intent.setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(Intent.createChooser(intent, "이미지를 선택하세요"), 0)
        }
        btn_upload.setOnClickListener {

            if(Edit_title.text.toString().length<=0 &&Edit_description.text.toString().length<=0) {
                Toast.makeText(this,"제목과 내용을 입력해주세요",Toast.LENGTH_LONG).show()
            }else {
                val adddata = FireData(title=Edit_title.text.toString(),description = Edit_description.text.toString(),imagename = filename)
                viewModel.addData(adddata)



            }

            uploadfile()



        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //request 코드가 0이고 ok를 선택했고 data에 뭔가가 들어있다면
        if (requestCode == 0 && resultCode == RESULT_OK) {
            filePath = data?.getData()
            try {
                //uri 파일을 bitmap으로 만들어서 imageView에 집어넣는다
                val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                image_preview.setImageBitmap(bitmap)

            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }

    fun uploadfile() {

        //업로드할 파일이 있으면 수행

        if(filePath!=null) {
            //업로드 진행 Dialog 보이기
            val progressDialog: ProgressDialog = ProgressDialog(this)
            progressDialog.setTitle("업로드중...")
            progressDialog.show();

            //storage
            val storage: FirebaseStorage = FirebaseStorage.getInstance()

            //Unique한 파일명을 만들자.
            val formatter: SimpleDateFormat = SimpleDateFormat("yyyyMMHH_mmss")
            val now: Date = Date()

            filename = formatter.format(now) + ".png"
            //storage 주소와 폴더 파일명을 지정해 준다.
            mStorageRef = storage.getReferenceFromUrl("gs://military-service-54f50.appspot.com")
                .child("images/" + filename)
            //올라가거라...
            mStorageRef!!.putFile(filePath!!)
                //성공시
                .addOnSuccessListener(OnSuccessListener<UploadTask.TaskSnapshot>() {
                    progressDialog.dismiss(); //업로드 진행 Dialog 상자 닫기
                    Toast.makeText(this, "업로드 완료!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this,Home::class.java))

                })
                //실패시
                .addOnFailureListener(OnFailureListener() {
                    progressDialog.dismiss()
                    Toast.makeText(
                        this,
                        "업로드 실패!" + filePath + "ggg" + mStorageRef,
                        Toast.LENGTH_SHORT
                    ).show()

                })
                //진행중
                .addOnProgressListener(OnProgressListener<UploadTask.TaskSnapshot>() {


                })
        }else {
            Toast.makeText(this, "업로드 완료!", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this,Home::class.java))

        }

    }

}

