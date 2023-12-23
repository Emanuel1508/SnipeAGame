package com.example.domain.usecases

import com.example.domain.repositories.AchievementsRepository

class GetAvailableAchievementsUseCase(private val achievementsRepository: AchievementsRepository) {
    suspend operator fun invoke(userId: String) =
        achievementsRepository.getAvailableAchievements(userId)
}