package com.junyoung.day

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class PageAdapter(fm:FragmentManager) : FragmentStatePagerAdapter(fm) {
    //뷰ㅜ페이저와 연동시킬 fragmnet 들을 모아둔 곳
    private var fragments : ArrayList<Fragment> = ArrayList()

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }

    fun addItems(fg:Fragment) {
        fragments.add(fg)
    }

}