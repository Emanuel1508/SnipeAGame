package com.example.domain.usecases

import com.example.domain.repositories.GamesRepository

class FinishGameUseCase(private val gamesRepository: GamesRepository) {
    suspend operator fun invoke(userId: String, gameId: String) =
        gamesRepository.finishGame(userId, gameId)
}