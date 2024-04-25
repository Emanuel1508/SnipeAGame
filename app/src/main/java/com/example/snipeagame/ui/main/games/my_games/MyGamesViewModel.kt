package com.example.snipeagame.ui.main.games.my_games

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.domain.models.GameParameters
import com.example.domain.usecases.FinishGameUseCase
import com.example.domain.usecases.GetMyGamesUseCase
import com.example.domain.usecases.GetUserIdUseCase
import com.example.domain.usecases.SaveGameToJournalUseCase
import com.example.domain.usecases.UserUpdateStatsUseCase
import com.example.domain.utils.ErrorMessage
import com.example.domain.utils.NumberConstants
import com.example.domain.utils.UseCaseResponse
import com.example.snipeagame.base.BaseViewModel
import com.example.snipeagame.utils.StringConstants
import com.example.snipeagame.utils.convertDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class MyGamesViewModel @Inject constructor(
    private val getMyGamesUseCase: GetMyGamesUseCase,
    private val getUserIdUseCase: GetUserIdUseCase,
    private val userUpdateStatsUseCase: UserUpdateStatsUseCase,
    private val finishGameUseCase: FinishGameUseCase,
    private val saveGameToJournalUseCase: SaveGameToJournalUseCase
) :
    BaseViewModel() {
    private val _myGames = MutableLiveData<List<GameParameters>>()
    val myGames: LiveData<List<GameParameters>> = _myGames
    private lateinit var id: String
    private val TAG: String = this::class.java.simpleName

    init {
        getUserId()
    }

    private fun getUserId() {
        showLoading()
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = getUserIdUseCase()) {
                is UseCaseResponse.Success -> {
                    getMyGames(result.body)
                    id = result.body
                }

                is UseCaseResponse.Failure -> onDataFailure(result.error)
            }
        }
    }

    private fun getMyGames(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val gamesResult = getMyGamesUseCase(userId)) {
                is UseCaseResponse.Success -> onGameDataSuccess(gamesResult.body)
                is UseCaseResponse.Failure -> onDataFailure(gamesResult.error)
            }
        }
    }

    private fun onGameDataSuccess(gameParameters: List<GameParameters>) {
        _myGames.postValue(gameParameters)
        hideLoading()
    }

    private fun onDataFailure(error: ErrorMessage) {
        showError(error)
        hideLoading()
    }

    fun formatDateAndTime(game: GameParameters) = convertDate(game)

    fun onGameFinish(takedowns: String, game: GameParameters, isChecked: Boolean) {
        showLoading()
        viewModelScope.launch(Dispatchers.IO) {
            when (val response = userUpdateStatsUseCase(takedowns.toInt(), id)) {
                is UseCaseResponse.Success -> {
                    if (isChecked) {
                        saveToJournal(game, takedowns)
                    } else {
                        when (val result = finishGameUseCase(id, game.gameId)) {
                            is UseCaseResponse.Success -> hideLoading()
                            is UseCaseResponse.Failure -> showError(result.error)
                        }
                    }
                }

                is UseCaseResponse.Failure -> onDataFailure(response.error)
            }
        }
    }

    private suspend fun saveToJournal(
        game: GameParameters,
        takedowns: String
    ) {
        when (val saveToJournal = saveGameToJournalUseCase(
            gameId = game.gameId,
            userId = id,
            location = game.location,
            date = game.date,
            time = game.time,
            numberOfPlayers = game.currentPlayers.toString(),
            takedowns = takedowns,
            rating = NumberConstants.ZERO_FLOAT.toString(),
            journalText = StringConstants.EMPTY_STRING
        )) {
            is UseCaseResponse.Success -> {
                finishGameUseCase(id, game.gameId)
                hideLoading()
            }

            is UseCaseResponse.Failure -> onDataFailure(saveToJournal.error)
        }
    }

    fun onRefresh() {
        getUserId()
    }

    fun checkGamePastDue(date: Date) =
        date.before(Calendar.getInstance().time)

}