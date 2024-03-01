package com.example.snipeagame.ui.main.games.create_game

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.domain.usecases.CreateGameUseCase
import com.example.domain.utils.ErrorMessage
import com.example.domain.utils.NumberConstants
import com.example.domain.utils.UseCaseResponse
import com.example.snipeagame.base.BaseViewModel
import com.example.snipeagame.utils.ButtonState
import com.example.snipeagame.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class CreateGameViewModel @Inject constructor(
    private val createGamesUseCase: CreateGameUseCase
) : BaseViewModel() {
    private val _navigation = SingleLiveEvent<ShowDialog>()
    val navigation: LiveData<ShowDialog> get() = _navigation
    private val _createButtonState = MutableLiveData<ButtonState>()
    val createButtonState: LiveData<ButtonState> get() = _createButtonState
    private var gameDateState: Boolean = false
    private var gameTimeState: Boolean = false
    private var gameLocationState: Boolean = false
    private var gameNumberOfPlayersState: Boolean = false
    private val TAG: String = this::class.java.simpleName

    fun onSelectDateButtonClick() {
        _navigation.value = ShowDialog.DateDialog
    }

    fun onSelectTimeButtonClick() {
        _navigation.value = ShowDialog.TimeDialog
    }

    fun onValidateDate(date: String) {
        val dateFormatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val selectedDate = dateFormatter.parse(date)
        if (selectedDate != null) {
            gameDateState = isDateValid(Calendar.getInstance().time, selectedDate)
            _createButtonState.value = if (gameDateState)
                ButtonState.IsEnabled
            else
                ButtonState.NotEnabled
        }
    }

    fun onValidateTime(timeData: String) {
        gameTimeState = timeData.isNotEmpty()
    }

    fun onValidateLocation(location: String) {
        gameLocationState = location.isNotEmpty()
    }

    fun onValidateNumberOfPlayers(numberOfPlayers: String) {
        gameNumberOfPlayersState = (numberOfPlayers.toInt() >= NumberConstants.MIN_PLAYERS
                && numberOfPlayers.toInt() <= NumberConstants.MAX_PLAYERS)
    }

    fun isButtonActive() {
        if (gameDateState && gameTimeState && gameLocationState && gameNumberOfPlayersState)
            _createButtonState.value = ButtonState.IsEnabled
        else
            _createButtonState.value = ButtonState.NotEnabled
    }

    suspend fun onCreateGameButtonPress(
        date: String,
        time: String,
        location: String,
        numberOfPlayers: String
    ) {
        showLoading()
        val gameCreationResponse = createGamesUseCase(
            gameId = UUID.randomUUID().toString(),
            date = date,
            time = time,
            location = location,
            numberOfPlayers = numberOfPlayers,
            currentPlayers = NumberConstants.ZERO
        )
        handleGameCreationResponse(gameCreationResponse)
    }

    private fun handleGameCreationResponse(response: UseCaseResponse<String>) {
        when (response) {
            is UseCaseResponse.Success -> onSuccess(response)
            is UseCaseResponse.Failure -> showErrorMessage(
                errorMessage = response.error,
                logMessage = "Game creation failed with error, ${response.error}"
            )
        }
    }

    private fun showErrorMessage(errorMessage: ErrorMessage, logMessage: String) {
        hideLoading()
        showError(errorMessage)
        Log.v(TAG, logMessage)
    }

    private fun onSuccess(response: UseCaseResponse<String>) {
        hideLoading()
        showSuccess(response)
    }

    private fun isDateValid(currentDate: Date, selectedDate: Date) =
        currentDate.before(selectedDate)

    sealed class ShowDialog {
        data object DateDialog : ShowDialog()
        data object TimeDialog : ShowDialog()
    }
}

