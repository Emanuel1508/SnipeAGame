package com.example.snipeagame.ui.main.achievements.available

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.domain.models.AchievementsParameters
import com.example.domain.usecases.GetAvailableAchievementsUseCase
import com.example.domain.usecases.GetUserIdUseCase
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
    private val getUserIdUseCase: GetUserIdUseCase
) : BaseViewModel() {
    private var _achievements = MutableLiveData<List<AchievementsParameters>>()
    val achievements: LiveData<List<AchievementsParameters>> = _achievements

    private val TAG = this::class.simpleName

    init {
        getAchievements()
    }

    private fun getAchievements() {
        showLoading()
        val userId = getUserId()
        viewModelScope.launch(Dispatchers.IO) {
            if (userId.isNotEmpty())
                fetchAchievements(userId)
            else
                hideLoading()
        }
    }

    private fun getUserId(): String {
        return when (val resultId = getUserIdUseCase()) {
            is UseCaseResponse.Success -> resultId.body
            is UseCaseResponse.Failure -> {
                showError(resultId.error)
                StringConstants.EMPTY_STRING
            }
        }
    }

    private suspend fun fetchAchievements(userId: String) {
        when (val result = getAvailableAchievementsUseCase(userId)) {
            is UseCaseResponse.Success -> onDataSuccess(result.body)
            is UseCaseResponse.Failure -> onDataFailure(result.error)
        }
    }

    private fun onDataSuccess(achievementsData: List<AchievementsParameters>) {
        _achievements.postValue(achievementsData)
        hideLoading()
    }

    private fun onDataFailure(error: ErrorMessage) {
        showError(error)
        hideLoading()
    }

    private fun saveUnlockedAchievement(userId: String, achievement: AchievementsParameters) {
        viewModelScope.launch(Dispatchers.IO) {
            unlockAchievementUseCase(userId, achievement)
        }
        hideLoading()
    }

    fun pendingUnlock(position: Int, stats: Int = 0) {
        val achievement = achievements.value?.get(position)
        achievement?.let {
            if (achievement.condition <= stats) {
                achievement.isButtonVisible = true
            }
        }
    }

    fun unlockAchievement(achievement: AchievementsParameters) {
        showLoading()
        val userId = getUserId()
        if (userId.isNotEmpty())
            saveUnlockedAchievement(userId, achievement)
        else
            hideLoading()
    }
}