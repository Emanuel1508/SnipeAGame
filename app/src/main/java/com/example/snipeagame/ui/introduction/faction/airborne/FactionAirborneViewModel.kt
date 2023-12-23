package com.example.snipeagame.ui.introduction.faction.airborne

import androidx.lifecycle.LiveData
import com.example.snipeagame.base.BaseViewModel
import com.example.snipeagame.utils.FactionNavDestination
import com.example.snipeagame.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FactionAirborneViewModel @Inject constructor() : BaseViewModel() {
    private val _navigation = SingleLiveEvent<FactionNavDestination>()
    val navigation: LiveData<FactionNavDestination> get() = _navigation

    fun onSelectButtonClick() {
        _navigation.value = FactionNavDestination.RolesScreen
    }
}