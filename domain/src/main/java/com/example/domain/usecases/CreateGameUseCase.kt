package com.example.domain.usecases

import com.example.domain.models.GameParameters
import com.example.domain.repositories.GamesRepository
import com.example.domain.utils.UseCaseResponse

class CreateGameUseCase(private val gamesRepository: GamesRepository) {
    suspend operator fun invoke(
        gameId:String,
        date: String,
        time: String,
        location: String,
        numberOfPlayers: String,
        currentPlayers: Int
    ): UseCaseResponse<String> {
        val gameParameters = GameParameters(
            gameId = gameId,
            date = date,
            time = time,
            location = location,
            numberOfPlayers = numberOfPlayers,
            currentPlayers = currentPlayers

        )
        return gamesRepository.createGame(gameParameters)
    }
}