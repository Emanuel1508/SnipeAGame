package com.example.domain.usecases

import com.example.domain.repositories.GamesRepository

class LeaveGameUseCase(private val repository: GamesRepository) {
    suspend operator fun invoke(userId: String, gameId: String) = repository.leaveGame(userId, gameId)
}