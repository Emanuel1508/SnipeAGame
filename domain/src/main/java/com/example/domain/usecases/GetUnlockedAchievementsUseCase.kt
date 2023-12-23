package com.example.domain.usecases

import com.example.domain.repositories.AchievementsRepository

class GetUnlockedAchievementsUseCase(private val achievementsRepository: AchievementsRepository) {
    suspend operator fun invoke(userId: String) =
        achievementsRepository.getUnlockedAchievements(userId)
}