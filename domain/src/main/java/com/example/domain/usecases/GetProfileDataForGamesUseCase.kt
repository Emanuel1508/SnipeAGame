package com.example.domain.usecases

import com.example.domain.repositories.UserRepository

class GetProfileDataForGamesUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(userId: String) =
        userRepository.getProfileDataForGames(userId)
}