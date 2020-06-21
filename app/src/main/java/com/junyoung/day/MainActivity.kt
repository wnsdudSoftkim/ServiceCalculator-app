package com.junyoung.day

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.delay

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (imageview_like_progress.getAnimation() == null) {
            val rotateAnimation :Animation = AnimationUtils.loadAnimation (getApplicationContext(), R.anim.ani_image)
            imageview_like_progress.startAnimation(rotateAnimation)
            imageview_like_progress.setAnimation(rotateAnimation)
        }
        Handler().postDelayed({
            startActivity(Intent(this, Login2::class.java))
        },500)








    }

    override fun onStart() {
        super.onStart()

    }
}
