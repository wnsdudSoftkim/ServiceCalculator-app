package com.junyoung.day

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.custom_tab_button.view.*
import kotlinx.android.synthetic.main.header.*
import kotlinx.android.synthetic.main.nav.*

class Home : AppCompatActivity() {
    private lateinit var mContext: Context
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        mContext = applicationContext
        initViewPager()
        auth = FirebaseAuth.getInstance()

        btn_open.setOnClickListener {
            drawer_layout.openDrawer(drawer)
        }
        btn_close.setOnClickListener {
            drawer_layout.closeDrawers()
        }
        btn_logout.setOnClickListener {
            auth.signOut()
            if (auth.currentUser == null) {
                startActivity(Intent(this, Login2::class.java))
            }
        }



    }
    //custom으로 Tab을 꾸며줌.
    private fun createView(tabName: String): View {
        var tabView = LayoutInflater.from(mContext).inflate(R.layout.custom_tab_button, null)
        tabView.tab_text.text = tabName
        when (tabName) {
            "홈" -> {
                tabView.tab_logo.setImageResource(R.mipmap.home)
            }
            "내 정보" -> {
                tabView.tab_logo.setImageResource(R.mipmap.person)
            }
            "목록" -> {
                tabView.tab_logo.setImageResource(R.mipmap.folder)
            }
            else -> return tabView
        }
        return tabView
    }

    private fun initViewPager() {
        val HomeFragment = HomeFragmentTab()
        val PersonFragment = PersonFragmentTab()
        val menuFragment = MenuFragmentTab()
        val adapter = PageAdapter(supportFragmentManager)
        //순서 대로 넣어줌
        adapter.addItems(HomeFragment)
        adapter.addItems(PersonFragment)
        adapter.addItems(menuFragment)
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)

        tabLayout.getTabAt(0)?.setCustomView(createView("홈"))
        tabLayout.getTabAt(1)?.setCustomView(createView("내 정보"))
        tabLayout.getTabAt(2)?.setCustomView(createView("목록"))


    }


}
