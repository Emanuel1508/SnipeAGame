package com.example.snipeagame.ui.main.achievements.available

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.domain.models.AchievementsParameters
import com.example.domain.models.UserStats
import com.example.domain.usecases.GetAvailableAchievementsUseCase
import com.example.domain.usecases.GetUserIdUseCase
import com.example.domain.usecases.GetUserStatsUseCase
import com.example.domain.usecases.UnlockAchievementUseCase
import com.example.domain.utils.ErrorMessage
import com.example.domain.utils.UseCaseResponse
import com.example.snipeagame.base.BaseViewModel
import com.example.snipeagame.utils.StringConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AvailableAchievementsViewModel @Inject constructor(
    private val getAvailableAchievementsUseCase: GetAvailableAchievementsUseCase,
    private val unlockAchievementUseCase: UnlockAchievementUseCase,
    private val getUserIdUseCase: GetUserIdUseCase,
    private val getUserStatsUseCase: GetUserStatsUseCase
) : BaseViewModel() {
    private var _achievements = MutableLiveData<List<AchievementsParameters>>()
    val achievements: LiveData<List<AchievementsParameters>> = _achievements

    private var achievementList: List<AchievementsParameters> = emptyList()

    private var userId: String = ""
    private var userStats: UserStats = UserStats()
    private val TAG = this::class.simpleName

    init {
        getUserId()
    }

    private fun getUserId() {
        return when (val resultId = getUserIdUseCase()) {
            is UseCaseResponse.Success -> {
                userId = resultId.body
                fetchUserStats(userId)
            }
            is UseCaseResponse.Failure -> showError(resultId.error)
        }
    }

    private fun fetchUserStats(userId: String) {
        showLoading()
        viewModelScope.launch(Dispatchers.IO) {
            when (val statsResponse = getUserStatsUseCase(userId)) {
                is UseCaseResponse.Success -> {
                    userStats = statsResponse.body
                    getAchievements(userId)
                }

                is UseCaseResponse.Failure -> onDataFailure(statsResponse.error)
            }
            hideLoading()
        }
    }

    private suspend fun getAchievements(userId: String) {
        if (userId.isNotEmpty())
            fetchAchievements(userId)
    }

    private suspend fun fetchAchievements(userId: String) {
        when (val result = getAvailableAchievementsUseCase(userId)) {
            is UseCaseResponse.Success -> {
                onDataSuccess(result.body)
            }

            is UseCaseResponse.Failure -> onDataFailure(result.error)
        }
    }

    private fun onDataSuccess(achievementsData: List<AchievementsParameters>) {
        achievementList = achievementsData
        _achievements.postValue(achievementList)
    }

    private fun onDataFailure(error: ErrorMessage) {
        showError(error)
    }

    private fun saveUnlockedAchievement(userId: String, achievement: AchievementsParameters) {
        viewModelScope.launch(Dispatchers.IO) {
            unlockAchievementUseCase(userId, achievement)
        }
    }

    fun pendingUnlock() {
        achievementList.forEach { achievement ->
            with(achievement) {
                if (type == StringConstants.TAKEDOWN && condition <= userStats.takedowns) {
                    isButtonVisible = true
                }
                if (type == StringConstants.GAME && condition <= userStats.games) {
                    isButtonVisible = true
                }
            }
        }
    }

    fun unlockAchievement(achievement: AchievementsParameters) {
        showLoading()
        if (userId.isNotEmpty())
            saveUnlockedAchievement(userId, achievement)
        hideLoading()
    }

    fun onRefresh() {
        getUserId()
    }
}