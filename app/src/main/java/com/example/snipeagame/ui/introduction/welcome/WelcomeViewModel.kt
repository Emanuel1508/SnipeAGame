package com.example.snipeagame.ui.introduction.welcome

import androidx.lifecycle.LiveData
import com.example.snipeagame.base.BaseViewModel
import com.example.snipeagame.utils.SingleLiveEvent

class WelcomeViewModel : BaseViewModel() {
    private val _navigation = SingleLiveEvent<NavDestination>()
    val navigation: LiveData<NavDestination> = _navigation

    fun onRegisterButtonPress() {
        _navigation.value = NavDestination.Register
    }

    fun onLoginButtonPress() {
        _navigation.value = NavDestination.Login
    }

    sealed class NavDestination {
        data object Register : NavDestination()
        data object Login : NavDestination()
    }
}