package com.example.snipeagame.ui.main.games.all_games

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.domain.models.GameParameters
import com.example.domain.usecases.GetGamesUseCase
import com.example.domain.utils.ErrorMessage
import com.example.domain.utils.UseCaseResponse
import com.example.snipeagame.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllGamesViewModel @Inject constructor(
    private val getGamesUseCase: GetGamesUseCase
) : BaseViewModel() {
    private val _games = MutableLiveData<List<GameParameters>>()
    val games: LiveData<List<GameParameters>> = _games

    init {
        getGames()
    }

    private fun getGames() {
        showLoading()
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = getGamesUseCase()) {
                is UseCaseResponse.Success -> onDataSuccess(result.body)
                is UseCaseResponse.Failure -> onDataFailure(result.error)
            }
        }
    }

    private fun onDataSuccess(gameParameters: List<GameParameters>) {
        _games.postValue(gameParameters)
        hideLoading()
    }

    private fun onDataFailure(error: ErrorMessage) {
        showError(error)
        hideLoading()
    }
}