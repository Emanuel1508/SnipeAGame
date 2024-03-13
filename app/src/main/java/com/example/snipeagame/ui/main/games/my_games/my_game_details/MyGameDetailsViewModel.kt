package com.example.snipeagame.ui.main.games.my_games.my_game_details

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.domain.models.UserGameDataParameters
import com.example.domain.usecases.GetPlayersUseCase
import com.example.domain.utils.ErrorMessage
import com.example.domain.utils.UseCaseResponse
import com.example.snipeagame.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyGameDetailsViewModel @Inject constructor(
    private val getPlayersUseCase: GetPlayersUseCase
) : BaseViewModel() {
    private val _playerList = MutableLiveData<List<UserGameDataParameters>>()
    val playerList: LiveData<List<UserGameDataParameters>> = _playerList
    private var gameId: String = ""
    private val TAG = this::class.java.simpleName

    fun setGameId(gameId: String) {
        this.gameId = gameId
        Log.v(TAG, gameId)
    }

    fun getPlayers() {
        showLoading()
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = getPlayersUseCase(gameId)) {
                is UseCaseResponse.Success -> onGameDataSuccess(result.body)
                is UseCaseResponse.Failure -> onDataFailure(result.error)
            }
        }
    }

    private fun onGameDataSuccess(playerData: List<UserGameDataParameters>) {
        _playerList.postValue(playerData)
        Log.v(TAG, _playerList.value.toString())
        hideLoading()
    }

    private fun onDataFailure(error: ErrorMessage) {
        showError(error)
        hideLoading()
    }
}