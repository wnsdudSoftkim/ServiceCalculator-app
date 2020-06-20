package com.junyoung.day

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        main_btn.setOnClickListener {
            onClick()
        }
    }
    fun onClick() {
        startActivity(
            Intent(
                this,Login2::class.java
            )
        )
    }
}
