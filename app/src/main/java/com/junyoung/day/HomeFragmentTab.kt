package com.junyoung.day


import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.method.TextKeyListener.clear

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast


import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_add_post.*
import kotlinx.android.synthetic.main.activity_home_fragment_tab.*
import kotlinx.coroutines.delay


import java.util.*


class HomeFragmentTab : Fragment() {
    private val viewModel: DayViewModel by viewModels()
    val storage: FirebaseStorage =
        FirebaseStorage.getInstance("gs://military-service-54f50.appspot.com")
    val storageRef: StorageReference = storage.getReference()
    val user = FirebaseAuth.getInstance().currentUser
    val pathReference: StorageReference = storageRef.child("images")


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel.getDataCalender()
        viewModel.getImageData()
        return inflater.inflate(R.layout.activity_home_fragment_tab, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_addPost.setOnClickListener {
            startActivity(Intent(activity,AddPost::class.java))
        }






        viewModel.Livedata.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            text_total_serve2.setText(viewModel.Livedata.value!![0].totalserve)
            text_has_serve2.setText(viewModel.Livedata.value!![0].hasserve)
            text_left_serve2.setText(viewModel.Livedata.value!![0].leftserve)
            text_progressbar2.setText(viewModel.Livedata.value!![0].progressbar)
            text_nickname2.setText(viewModel.Livedata.value!![0].nickname)
            text_myname2.setText(viewModel.Livedata.value!![0].myname)
            val pro = viewModel.Livedata.value!![0].progressbar!!.toInt()
            progressBar2.setProgress(pro)



        })
        viewModel.Livedata2.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            val path = pathReference.child(viewModel.Livedata2.value!![0].imagedata.toString())
                Glide.with(this)
                    .load(path)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(image_home)







        })


    }

    fun delay(a: Int) {
        delay(a)
    }


}
