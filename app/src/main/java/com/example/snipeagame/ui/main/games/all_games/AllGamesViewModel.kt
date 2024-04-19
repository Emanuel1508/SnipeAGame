package com.example.snipeagame.ui.main.games.all_games

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.domain.models.GameParameters
import com.example.domain.models.UserGameDataParameters
import com.example.domain.models.containsFilteringString
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

    private val _gameListStatus = MutableLiveData<GameListState>()
    val gameListStatus: LiveData<GameListState> = _gameListStatus

    private var filteredGames: List<GameParameters> = emptyList()

    private lateinit var id: String
    private val TAG: String = this::class.java.simpleName

    init {
        getUserId()
    }

    private fun getUserId() {
        when (val result = getUserIdUseCase()) {
            is UseCaseResponse.Success -> {
                getGames(result.body)
                id = result.body
            }

            is UseCaseResponse.Failure -> onDataFailure(result.error)
        }
    }

    private fun getGames(userId: String) {
        showLoading()
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = getGamesUseCase(userId)) {
                is UseCaseResponse.Success -> onGameDataSuccess(result.body)
                is UseCaseResponse.Failure -> onDataFailure(result.error)
            }
        }
    }

    fun onJoinGameButtonPress(game: GameParameters) {
        viewModelScope.launch(Dispatchers.IO) {
            getUserGameData(id, game)
        }
    }

    private fun getUserGameData(userId: String, game: GameParameters) {
        showLoading()
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = getProfileDataForGamesUseCase(userId)) {
                is UseCaseResponse.Success -> {
                    joinGame(game, result, userId)
                }

                is UseCaseResponse.Failure -> onDataFailure(result.error)
            }
        }
    }

    private suspend fun joinGame(
        game: GameParameters,
        result: UseCaseResponse.Success<UserGameDataParameters>,
        userId: String
    ) {
        game.currentPlayers++

        when (val response = joinGameUseCase(result.body, game, userId)) {
            is UseCaseResponse.Success -> hideLoading()
            is UseCaseResponse.Failure -> onDataFailure(response.error)
        }
    }

    private fun onGameDataSuccess(gameParameters: List<GameParameters>) {
        filteredGames = gameParameters
        _games.postValue(filteredGames)
        hideLoading()
    }

    private fun onDataFailure(error: ErrorMessage) {
        showError(error)
        hideLoading()
    }

    private fun verifyGames() {
        _gameListStatus.value =
            if (_games.value?.isEmpty() == true)
                GameListState.NotPopulated
            else
                GameListState.IsPopulated
    }

    fun onFilterGames(filteringString: String) {
        showLoading()
        if (filteringString.isEmpty()) {
            _games.value = filteredGames
        }
        _games.value = _games.value?.filter { gameEntity ->
            gameEntity.containsFilteringString(filteringString)
        }
        verifyGames()
        hideLoading()
    }

    fun onRefresh() {
        getGames(id)
    }

    sealed class GameListState {
        data object IsPopulated : GameListState()
        data object NotPopulated : GameListState()
    }
}