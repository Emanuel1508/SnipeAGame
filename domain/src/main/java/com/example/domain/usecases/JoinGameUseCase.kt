package com.example.domain.usecases

import com.example.domain.models.GameParameters
import com.example.domain.models.UserGameDataParameters
import com.example.domain.repositories.GamesRepository

class JoinGameUseCase(private var gamesRepository: GamesRepository) {
    suspend operator fun invoke(user: UserGameDataParameters, game: GameParameters, userId: String) =
        gamesRepository.joinGame(user, game, userId)
}