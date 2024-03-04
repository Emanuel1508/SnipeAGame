package com.example.data.repository

import android.util.Log
import com.example.data.utils.DatabaseConstants
import com.example.data.utils.StringConstants
import com.example.domain.models.GameParameters
import com.example.domain.models.UserGameDataParameters
import com.example.domain.repositories.GamesRepository
import com.example.domain.utils.ErrorMessage
import com.example.domain.utils.UseCaseResponse
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await
import java.net.UnknownHostException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class GamesRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : GamesRepository {
    private val TAG: String = this::class.java.simpleName
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

    override suspend fun getAllGames(userId: String): UseCaseResponse<List<GameParameters>> {
        return try {
            val gamesDocumentSnapshot = firestore.collection(DatabaseConstants.ALL_GAMES)
                .get()
                .await()
            var games = documentToGameObject(gamesDocumentSnapshot)
            games = deleteGamesPastDate(games)

            val myGamesDocumentSnapshot = firestore.collection(DatabaseConstants.PROFILES)
                .document(userId)
                .collection(DatabaseConstants.MY_GAMES)
                .get()
                .await()

            val myGames = documentToGameObject(myGamesDocumentSnapshot)
            games = retainUnjoinedGames(games, myGames)

            UseCaseResponse.Success(games)
        } catch (unknownHostException: UnknownHostException) {
            unknownHostException.getGamesError(ErrorMessage.NO_NETWORK)
        } catch (exception: Exception) {
            exception.getGamesError(ErrorMessage.GENERAL)
        }
    }

    override suspend fun joinGame(
        user: UserGameDataParameters,
        game: GameParameters,
        userId: String
    ): UseCaseResponse<String> {
        return try {
            saveUserToAllGames(user, game, userId)
            saveGameToMyGames(game, userId)
            increaseCurrentPlayersNumber(game)
            UseCaseResponse.Success(StringConstants.SUCCESS)
        } catch (unknownHostException: UnknownHostException) {
            unknownHostException.getGamesError(ErrorMessage.NO_NETWORK)
        } catch (exception: Exception) {
            exception.getGamesError(ErrorMessage.GENERAL)
        }
    }

    override suspend fun getMyGames(userId: String): UseCaseResponse<List<GameParameters>> {
        return try {
            val myGamesDocumentSnapshot = firestore.collection(DatabaseConstants.PROFILES)
                .document(userId)
                .collection(DatabaseConstants.MY_GAMES)
                .get()
                .await()
            val myGames = documentToGameObject(myGamesDocumentSnapshot)
            UseCaseResponse.Success(myGames)
        } catch (unknownHostException: UnknownHostException) {
            unknownHostException.getGamesError(ErrorMessage.NO_NETWORK)
        } catch (exception: Exception) {
            exception.getGamesError(ErrorMessage.GENERAL)
        }
    }

    private fun saveUserToAllGames(
        user: UserGameDataParameters,
        game: GameParameters,
        userId: String
    ) {
        firestore.collection(DatabaseConstants.ALL_GAMES)
            .document(game.gameId)
            .collection(DatabaseConstants.PLAYERS)
            .document(userId)
            .set(user)
    }

    private fun saveGameToMyGames(
        game: GameParameters,
        userId: String
    ) {
        firestore.collection(DatabaseConstants.PROFILES)
            .document(userId)
            .collection(DatabaseConstants.MY_GAMES)
            .document(game.gameId)
            .set(game)
    }

    private fun increaseCurrentPlayersNumber(game: GameParameters) {
        firestore.collection(DatabaseConstants.ALL_GAMES)
            .document(game.gameId)
            .update(DatabaseConstants.CURRENT_PLAYERS, game.currentPlayers)
    }

    private fun documentToGameObject(documents: QuerySnapshot): List<GameParameters> {
        val list: ArrayList<GameParameters> = ArrayList()
        for (document in documents) {
            Log.v(TAG, document.toObject(GameParameters::class.java).toString())
            list.add(document.toObject(GameParameters::class.java))
        }
        return list
    }

    private fun deleteGamesPastDate(games: List<GameParameters>): List<GameParameters> {
        games.forEach { game ->
            val formattedGameDate = formatDateAndTime(game)
            if (formattedGameDate.before(Calendar.getInstance().time)) {
                firestore.collection(DatabaseConstants.ALL_GAMES)
                    .document(game.gameId)
                    .delete()
            }
        }
        return games
    }

    private fun retainUnjoinedGames(
        joinedGames: List<GameParameters>,
        unjoinedGames: List<GameParameters>
    ) = joinedGames.filterNot { it in unjoinedGames }


    private fun formatDateAndTime(game: GameParameters): Date {
        val dateFormatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val timeFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())
        val formattedGameDate = dateFormatter.parse(game.date)
        val formattedGameTime = timeFormatter.parse(game.time)
        val calendar = Calendar.getInstance()

        formattedGameDate?.let { date ->
            formattedGameTime?.let { time ->
                calendar.time = date
                calendar.set(Calendar.HOUR_OF_DAY, time.hours)
                calendar.set(Calendar.MINUTE, time.minutes)
            }
        }
        val combinedDate = calendar.time
        Log.v(TAG, combinedDate.toString())

        return combinedDate
    }

    private fun java.lang.Exception.getGamesError(error: ErrorMessage): UseCaseResponse.Failure {
        return UseCaseResponse.Failure(error)
    }
}