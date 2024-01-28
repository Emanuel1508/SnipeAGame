package com.example.domain.repositories

import com.example.domain.models.GameParameters
import com.example.domain.utils.UseCaseResponse

interface GamesRepository {
    suspend fun createGame(gameData: GameParameters): UseCaseResponse<String>

    suspend fun getAllGames():UseCaseResponse<List<GameParameters>>

    suspend fun joinGame(userId:String, gameId: String): UseCaseResponse<String>
}