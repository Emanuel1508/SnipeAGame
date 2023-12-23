package com.example.snipeagame.ui.main.achievements.unlocked

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.domain.models.AchievementsParameters
import com.example.domain.usecases.GetUnlockedAchievementsUseCase
import com.example.domain.usecases.GetUserIdUseCase
import com.example.domain.utils.ErrorMessage
import com.example.domain.utils.UseCaseResponse
import com.example.snipeagame.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UnlockedAchievementsViewModel @Inject constructor(
    private val getUserIdUseCase: GetUserIdUseCase,
    private val getUnlockedAchievementsUseCase: GetUnlockedAchievementsUseCase
) : BaseViewModel() {
    private var _unlockedAchievements = MutableLiveData<List<AchievementsParameters>>()
    val unlockedAchievements: LiveData<List<AchievementsParameters>> = _unlockedAchievements

    private val TAG = this::class.simpleName

    init {
        getUserId()
    }

    private fun getUnlockedAchievements(userId: String) {
        showLoading()
        viewModelScope.launch(Dispatchers.IO) {
            when (val achievementsResult = getUnlockedAchievementsUseCase(userId)) {
                is UseCaseResponse.Success -> onFetchSuccess(achievementsResult.body)
                is UseCaseResponse.Failure -> onFetchFailure(achievementsResult.error)
            }
        }
    }

    private fun getUserId() {
        when (val responseId = getUserIdUseCase()) {
            is UseCaseResponse.Success -> getUnlockedAchievements(responseId.body)
            is UseCaseResponse.Failure -> showError(responseId.error)
        }
    }

    private fun onFetchSuccess(achievements: List<AchievementsParameters>) {
        hideLoading()
        _unlockedAchievements.postValue(achievements)
    }

    private fun onFetchFailure(error: ErrorMessage) {
        hideLoading()
        showError(error)
    }
}