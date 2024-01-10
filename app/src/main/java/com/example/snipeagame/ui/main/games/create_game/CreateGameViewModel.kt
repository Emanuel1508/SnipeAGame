package com.example.snipeagame.ui.main.games.create_game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.snipeagame.base.BaseViewModel
import com.example.snipeagame.utils.ButtonState
import com.example.snipeagame.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class CreateGameViewModel @Inject constructor() : BaseViewModel() {
    private val _navigation = SingleLiveEvent<ShowDialog>()
    val navigation: LiveData<ShowDialog> get() = _navigation
    private val _createButtonState = MutableLiveData<ButtonState>()
    val createButtonState: LiveData<ButtonState> get() = _createButtonState
    private var gameDateState: Boolean = false
    private var gameTimeState: Boolean = false
    private var gameLocationState: Boolean = false

    fun onSelectDateButtonClick() {
        _navigation.value = ShowDialog.DateDialog
    }

    fun onSelectTimeButtonClick() {
        _navigation.value = ShowDialog.TimeDialog
    }

    fun onValidateDate(date: String) {
        val dateFormatter = SimpleDateFormat("dd-mm-yyyy", Locale.getDefault())
        val selectedDate = dateFormatter.parse(date)
        if (selectedDate != null) {
            gameDateState = isDateValid(Calendar.getInstance().time, selectedDate)
            _createButtonState.value = if (gameDateState)
                ButtonState.IsEnabled
            else
                ButtonState.NotEnabled
        }
    }

    fun onValidateLocation(location: String) {
        gameLocationState = location.isNotEmpty()
    }

    fun onValidateTime(timeData: String) {
        gameTimeState = timeData.isNotEmpty()
    }

    fun isButtonActive() {
        if (gameDateState && gameTimeState && gameLocationState)
            _createButtonState.value = ButtonState.IsEnabled
        else
            _createButtonState.value = ButtonState.NotEnabled
    }

    private fun isDateValid(currentDate: Date, selectedDate: Date) =
        currentDate.before(selectedDate)

    sealed class ShowDialog {
        data object DateDialog : ShowDialog()
        data object TimeDialog : ShowDialog()
    }
}

