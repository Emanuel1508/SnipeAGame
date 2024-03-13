package com.example.domain.usecases

import com.example.domain.repositories.GamesRepository

class GetPlayersUseCase(private val repository: GamesRepository) {
    suspend operator fun invoke(gameId: String) =
        repository.getPlayers(gameId)
}
