package com.example.snipeagame.ui.main.games.my_games.my_game_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.domain.models.UserGameDataParameters
import com.example.domain.usecases.GetPlayersUseCase
import com.example.domain.usecases.GetUserIdUseCase
import com.example.domain.usecases.LeaveGameUseCase
import com.example.domain.utils.ErrorMessage
import com.example.domain.utils.UseCaseResponse
import com.example.snipeagame.base.BaseViewModel
import com.example.snipeagame.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyGameDetailsViewModel @Inject constructor(
    private val getPlayersUseCase: GetPlayersUseCase,
    private val getUserIdUseCase: GetUserIdUseCase,
    private val leaveGameUseCase: LeaveGameUseCase
) : BaseViewModel() {
    private val _playerList = MutableLiveData<List<UserGameDataParameters>>()
    val playerList: LiveData<List<UserGameDataParameters>> = _playerList
    private val _navigation = SingleLiveEvent<LeaveGameRedirect>()
    val navigation: LiveData<LeaveGameRedirect> = _navigation
    private val _gameState = MutableLiveData<UiState>()
    val gameState: LiveData<UiState> = _gameState
    private var gameId: String = ""
    private val TAG = this::class.java.simpleName

    private fun onGameDataSuccess(playerData: List<UserGameDataParameters>) {
        _playerList.postValue(playerData)
        hideLoading()
    }

    private fun onDataFailure(error: ErrorMessage) {
        showError(error)
        hideLoading()
    }

    fun leaveGame() {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = getUserIdUseCase()) {
                is UseCaseResponse.Success -> {
                    when (val response = leaveGameUseCase(result.body, gameId)) {
                        is UseCaseResponse.Success -> _navigation.postValue(
                            LeaveGameRedirect.GameFragment
                        )

                        is UseCaseResponse.Failure -> onDataFailure(response.error)
                    }
                }

                is UseCaseResponse.Failure -> onDataFailure(result.error)
            }
        }
    }

    fun setGameId(gameId: String) {
        this.gameId = gameId
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

    fun updateMyGamesUI(isGameCompleted: Boolean) {
        _gameState.value = if (isGameCompleted) UiState.GameCompleted else UiState.GameInProgress
    }

    sealed class LeaveGameRedirect {
        data object GameFragment : LeaveGameRedirect()
    }

    sealed class UiState {
        data object GameInProgress : UiState()
        data object GameCompleted : UiState()
    }
}