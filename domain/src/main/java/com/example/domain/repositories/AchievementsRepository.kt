package com.example.domain.repositories

import com.example.domain.models.AchievementsParameters
import com.example.domain.utils.UseCaseResponse

interface AchievementsRepository {
    suspend fun getAvailableAchievements(userId: String): UseCaseResponse<List<AchievementsParameters>>

    suspend fun unlockAchievement(
        userId: String,
        achievement: AchievementsParameters
    ): UseCaseResponse<String>

    suspend fun getUnlockedAchievements(userId: String): UseCaseResponse<List<AchievementsParameters>>
}
