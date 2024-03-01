package com.example.snipeagame.ui.main.games.all_games

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.domain.models.GameParameters
import com.example.domain.usecases.GetGamesUseCase
import com.example.domain.usecases.GetProfileDataForGamesUseCase
import com.example.domain.usecases.GetUserIdUseCase
import com.example.domain.usecases.JoinGameUseCase
import com.example.domain.utils.ErrorMessage
import com.example.domain.utils.UseCaseResponse
import com.example.snipeagame.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllGamesViewModel @Inject constructor(
    private val getGamesUseCase: GetGamesUseCase,
    private val getUserIdUseCase: GetUserIdUseCase,
    private val getProfileDataForGamesUseCase: GetProfileDataForGamesUseCase,
    private val joinGameUseCase: JoinGameUseCase
) : BaseViewModel() {
    private val _games = MutableLiveData<List<GameParameters>>()
    val games: LiveData<List<GameParameters>> = _games
    private val TAG: String = this::class.java.simpleName

    init {
        getGames()
    }

    private fun getGames() {
        showLoading()
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = getGamesUseCase()) {
                is UseCaseResponse.Success -> onGameDataSuccess(result.body)
                is UseCaseResponse.Failure -> onDataFailure(result.error)
            }
        }
    }

    fun onJoinGameButtonPress() {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = getUserIdUseCase()) {
                is UseCaseResponse.Success -> getUserGameData(result.body)
                is UseCaseResponse.Failure -> onDataFailure(result.error)
            }
        }
    }

    private fun getUserGameData(userId: String) {
        showLoading()
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = getProfileDataForGamesUseCase(userId)) {
                is UseCaseResponse.Success -> Log.v(TAG, "Succes: ${result.body}")
                is UseCaseResponse.Failure -> showError(result.error)
            }
            hideLoading()
        }
    }

    private fun onGameDataSuccess(gameParameters: List<GameParameters>) {
        _games.postValue(gameParameters)
        hideLoading()
    }

    private fun onDataFailure(error: ErrorMessage) {
        showError(error)
        hideLoading()
    }
}