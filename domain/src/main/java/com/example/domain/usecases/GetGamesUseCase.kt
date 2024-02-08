package com.example.domain.usecases

import com.example.domain.repositories.GamesRepository

class GetGamesUseCase(private val gamesRepository: GamesRepository) {
    suspend operator fun invoke() = gamesRepository.getAllGames()
}