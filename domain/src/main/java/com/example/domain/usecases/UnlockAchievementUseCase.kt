package com.example.domain.usecases

import com.example.domain.models.AchievementsParameters
import com.example.domain.repositories.AchievementsRepository

class UnlockAchievementUseCase(private val achievementsRepository: AchievementsRepository) {
    suspend operator fun invoke(userId: String, achievement: AchievementsParameters) =
        achievementsRepository.unlockAchievement(userId, achievement)
}