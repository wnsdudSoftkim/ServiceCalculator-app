package com.junyoung.day

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.NonNull
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FileDownloadTask
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_show_post.*
import java.io.File
import java.io.IOException

class ShowPost : AppCompatActivity() {

    val storage: FirebaseStorage =
        FirebaseStorage.getInstance("gs://military-service-54f50.appspot.com")
    val storageRef: StorageReference = storage.getReference()
    val user = FirebaseAuth.getInstance().currentUser
    val pathReference: StorageReference = storageRef.child("images")
    private  val viewModel : DayViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_post)


        val title = intent.getStringExtra("title")
        Toast.makeText(this,title,Toast.LENGTH_LONG).show()
        val description = intent.getStringExtra("description")
        val imagename = intent.getStringExtra("imagename")


        val filename =imagename
        val path = pathReference.child(filename)

        text_showpost_title.setText(title)
        text_showpost_description.setText(description)



        Glide.with(this)
            .load(path)
            .into(image_showpost)






    }

}
