package com.example.domain.repositories

import com.example.domain.models.GameParameters
import com.example.domain.models.UserGameDataParameters
import com.example.domain.utils.UseCaseResponse

interface GamesRepository {
    suspend fun createGame(gameData: GameParameters): UseCaseResponse<String>

    suspend fun getAllGames(userId: String): UseCaseResponse<List<GameParameters>>

    suspend fun joinGame(
        user: UserGameDataParameters,
        game: GameParameters,
        userId: String
    ): UseCaseResponse<String>

    suspend fun getMyGames(userId: String): UseCaseResponse<List<GameParameters>>

    suspend fun getPlayers(gameId: String): UseCaseResponse<List<UserGameDataParameters>>

    suspend fun leaveGame(userId: String, gameId: String): UseCaseResponse<String>
}