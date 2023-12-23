package com.example.snipeagame.ui.main.achievements

import android.util.Log
import com.example.snipeagame.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AchievementsViewModel @Inject constructor(
) : BaseViewModel() {
    fun testFun() {
        Log.v("ma0ta", "ma-ta")
    }
}