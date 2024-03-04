package com.example.domain.usecases

import com.example.domain.repositories.GamesRepository

class GetMyGamesUseCase(private val repository: GamesRepository) {
    suspend operator fun invoke(userId: String) = repository.getMyGames(userId)
}