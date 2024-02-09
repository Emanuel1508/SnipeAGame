package com.example.data.repository

import android.util.Log
import com.example.data.utils.DatabaseConstants
import com.example.data.utils.StringConstants
import com.example.domain.models.GameParameters
import com.example.domain.repositories.GamesRepository
import com.example.domain.utils.ErrorMessage
import com.example.domain.utils.UseCaseResponse
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await
import java.net.UnknownHostException
import javax.inject.Inject

class GamesRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : GamesRepository {
    private val TAG: String = this::class.java.simpleName
    override suspend fun createGame(gameData: GameParameters): UseCaseResponse<String> {
        return try {
            firestore.collection(DatabaseConstants.ALL_GAMES)
                .document(gameData.gameId)
                .collection(DatabaseConstants.GAME_DATA)
                .document()
                .set(gameData)
            UseCaseResponse.Success(StringConstants.SUCCESS)
        } catch (unknownHostException: UnknownHostException) {
            unknownHostException.getGamesError(ErrorMessage.NO_NETWORK)
        } catch (exception: Exception) {
            exception.getGamesError(ErrorMessage.GENERAL)
        }
    }

    override suspend fun getAllGames(): UseCaseResponse<List<GameParameters>> {
        return try {
            val allGamesDocumentSnapshot = firestore.collection(DatabaseConstants.ALL_GAMES)
                .get()
                .await()

            Log.v(TAG, allGamesDocumentSnapshot.documents.size.toString())

            UseCaseResponse.Success(mutableListOf())
        } catch (unknownHostException: UnknownHostException) {
            unknownHostException.getGamesError(ErrorMessage.NO_NETWORK)
        } catch (exception: Exception) {
            exception.getGamesError(ErrorMessage.GENERAL)
        }
    }

    override suspend fun joinGame(userId: String, gameId: String): UseCaseResponse<String> {
        TODO("Not yet implemented")
    }

    private fun java.lang.Exception.getGamesError(error: ErrorMessage): UseCaseResponse.Failure {
        return UseCaseResponse.Failure(error)
    }

    private fun documentToGameObject(documents: QuerySnapshot): List<GameParameters> {
        val list: ArrayList<GameParameters> = ArrayList()
        for (document in documents) {
            Log.v(TAG, document.toObject(GameParameters::class.java).toString())
            list.add(document.toObject(GameParameters::class.java))
        }
        return list
    }
}