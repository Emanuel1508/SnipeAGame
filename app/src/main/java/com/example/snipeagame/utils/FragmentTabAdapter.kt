package com.example.snipeagame.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class FragmentTabAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    private var fragmentList: MutableList<Fragment> = ArrayList()
    private var fragmentTitleList: MutableList<Int> = ArrayList()
    override fun getItemCount() = fragmentList.size

    override fun createFragment(position: Int) =
        fragmentList[position]

    fun addFragment(fragment: Fragment, stringResource: Int) {
        fragmentList.add(fragment)
        fragmentTitleList.add(stringResource)
    }

    fun getTabTitle(position: Int) = fragmentTitleList[position]
}