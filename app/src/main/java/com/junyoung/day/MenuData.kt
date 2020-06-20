package com.junyoung.day

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.menudata.*

class MenuData:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.menudata)
        data_title.setOnClickListener {
            startActivity(Intent(this,ShowPost::class.java))
        }
    }
}