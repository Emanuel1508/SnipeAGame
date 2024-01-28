package com.example.data.repository

import com.example.data.utils.DatabaseConstants
import com.example.data.utils.StringConstants
import com.example.domain.models.GameParameters
import com.example.domain.repositories.GamesRepository
import com.example.domain.utils.ErrorMessage
import com.example.domain.utils.UseCaseResponse
import com.google.firebase.firestore.FirebaseFirestore
import java.net.UnknownHostException
import javax.inject.Inject

class GamesRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : GamesRepository {
    override suspend fun createGame(gameData: GameParameters): UseCaseResponse<String> {
        return try {
            firestore.collection(DatabaseConstants.ALL_GAMES)
                .document(gameData.gameId)
                .set(gameData)
            UseCaseResponse.Success(StringConstants.SUCCESS)
        } catch (unknownHostException: UnknownHostException) {
            unknownHostException.getGamesError(ErrorMessage.NO_NETWORK)
        } catch (exception: Exception) {
            exception.getGamesError(ErrorMessage.GENERAL)
        }
    }

    override suspend fun getAllGames(): UseCaseResponse<List<GameParameters>> {
        TODO("Not yet implemented")
    }

    override suspend fun joinGame(userId: String, gameId: String): UseCaseResponse<String> {
        TODO("Not yet implemented")
    }

    private fun java.lang.Exception.getGamesError(error: ErrorMessage): UseCaseResponse.Failure {
        return UseCaseResponse.Failure(error)
    }
}