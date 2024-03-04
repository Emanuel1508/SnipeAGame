package com.example.snipeagame.ui.main.games.my_games

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.domain.models.GameParameters
import com.example.domain.usecases.GetMyGamesUseCase
import com.example.domain.usecases.GetUserIdUseCase
import com.example.domain.utils.ErrorMessage
import com.example.domain.utils.UseCaseResponse
import com.example.snipeagame.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyGamesViewModel @Inject constructor(
    private val getMyGamesUseCase: GetMyGamesUseCase,
    private val getUserIdUseCase: GetUserIdUseCase
) :
    BaseViewModel() {
    private val _myGames = MutableLiveData<List<GameParameters>>()
    val myGames: LiveData<List<GameParameters>> = _myGames

    init {
        getUserId()
    }

    private fun getUserId() {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = getUserIdUseCase()) {
                is UseCaseResponse.Success -> getMyGames(result.body)
                is UseCaseResponse.Failure -> onDataFailure(result.error)
            }
        }
    }

    private fun getMyGames(userId: String) {
        showLoading()
        viewModelScope.launch(Dispatchers.IO) {
            when(val gamesResult = getMyGamesUseCase(userId)) {
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
}