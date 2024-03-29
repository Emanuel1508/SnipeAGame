package com.example.domain.usecases

import com.example.domain.repositories.UserRepository

class UserUpdateStatsUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(takedowns: Int, userId: String) =
        userRepository.updateStats(takedowns, userId)
}