package com.example.data.repository

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
            games = deleteGamesPastDate(games.toMutableList())

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
            var myGames = documentToGameObject(myGamesDocumentSnapshot)
            myGames = syncPlayerNumbers(myGames)
            UseCaseResponse.Success(myGames)
        } catch (unknownHostException: UnknownHostException) {
            unknownHostException.getGamesError(ErrorMessage.NO_NETWORK)
        } catch (exception: Exception) {
            exception.getGamesError(ErrorMessage.GENERAL)
        }
    }

    override suspend fun getPlayers(gameId: String): UseCaseResponse<List<UserGameDataParameters>> {
        return try {
            val playersDocumentSnapshot = firestore.collection(DatabaseConstants.ALL_GAMES)
                .document(gameId)
                .collection(DatabaseConstants.PLAYERS)
                .get()
                .await()
            val players = documentToPlayerObject(playersDocumentSnapshot)
            UseCaseResponse.Success(players)
        } catch (unknownHostException: UnknownHostException) {
            unknownHostException.getGamesError(ErrorMessage.NO_NETWORK)
        } catch (exception: Exception) {
            exception.getGamesError(ErrorMessage.GENERAL)
        }
    }

    override suspend fun leaveGame(userId: String, gameId: String): UseCaseResponse<String> {
        return try {
            removeLeftGame(userId, gameId)
            removePlayer(gameId, userId)
            decreasePlayerNumbers(gameId)

            UseCaseResponse.Success(StringConstants.SUCCESS)
        } catch (unknownHostException: UnknownHostException) {
            unknownHostException.getGamesError(ErrorMessage.NO_NETWORK)
        } catch (exception: Exception) {
            exception.getGamesError(ErrorMessage.GENERAL)
        }
    }

    private fun removeLeftGame(userId: String, gameId: String) {
        firestore.collection(DatabaseConstants.PROFILES)
            .document(userId)
            .collection(DatabaseConstants.MY_GAMES)
            .document(gameId)
            .delete()
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
            list.add(document.toObject(GameParameters::class.java))
        }
        return list
    }

    private suspend fun deleteGamesPastDate(games: MutableList<GameParameters>): List<GameParameters> {
        games.forEach { game ->
            val playerDocumentSnapshot = fetchPlayerDocuments(game)
            val playerIds = getPlayerIds(playerDocumentSnapshot)
            val formattedGameDate = formatDateAndTime(game)

            if (formattedGameDate.before(Calendar.getInstance().time)) {
                deletePlayers(playerIds, game)
                deleteGame(game)
                games.remove(game)
            }
        }
        return games
    }

    private suspend fun fetchPlayerDocuments(game: GameParameters): QuerySnapshot {
        return firestore.collection(DatabaseConstants.ALL_GAMES)
            .document(game.gameId)
            .collection(DatabaseConstants.PLAYERS)
            .get()
            .await()
    }

    private fun retainUnjoinedGames(
        joinedGames: List<GameParameters>,
        unjoinedGames: List<GameParameters>
    ) = joinedGames.filterNot { joinedGame ->
        unjoinedGames.any { it.gameId == joinedGame.gameId }
    }

    private fun getPlayerIds(documents: QuerySnapshot): List<String> {
        val playerId: ArrayList<String> = ArrayList()
        for (document in documents) {
            playerId.add(document.id)
        }
        return playerId
    }

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
        return calendar.time
    }

    private fun deletePlayers(
        playerIds: List<String>,
        game: GameParameters
    ) {
        playerIds.forEach { playerId ->
            firestore.collection(DatabaseConstants.ALL_GAMES)
                .document(game.gameId)
                .collection(DatabaseConstants.PLAYERS)
                .document(playerId)
                .delete()
        }
    }

    private fun removePlayer(gameId: String, userId: String) {
        firestore.collection(DatabaseConstants.ALL_GAMES)
            .document(gameId)
            .collection(DatabaseConstants.PLAYERS)
            .document(userId)
            .delete()
    }

    private fun deleteGame(game: GameParameters) {
        firestore.collection(DatabaseConstants.ALL_GAMES)
            .document(game.gameId)
            .delete()
    }

    private suspend fun syncPlayerNumbers(games: List<GameParameters>): List<GameParameters> {
        games.forEach { game ->
            val formattedDate = formatDateAndTime(game)
            if (formattedDate.after(Calendar.getInstance().time)) {
                val document = firestore.collection(DatabaseConstants.ALL_GAMES)
                    .document(game.gameId)
                    .get()
                    .await()
                val number = document[DatabaseConstants.CURRENT_PLAYERS].toString()
                game.currentPlayers = Integer.valueOf(number)
            }
        }
        return games
    }

    private suspend fun decreasePlayerNumbers(gameId: String) {
        val numberOfPlayers = getPlayerCount(gameId)
        firestore.collection(DatabaseConstants.ALL_GAMES)
            .document(gameId)
            .update(DatabaseConstants.CURRENT_PLAYERS, numberOfPlayers)
    }

    private suspend fun getPlayerCount(gameId: String): Long? {
        val documentSnapshot = firestore.collection(DatabaseConstants.ALL_GAMES)
            .document(gameId)
            .get()
            .await()
        var numberOfPlayers = documentSnapshot.getLong(DatabaseConstants.CURRENT_PLAYERS)
        if (numberOfPlayers != null) {
            numberOfPlayers -= 1
        }
        return numberOfPlayers
    }

    private fun documentToPlayerObject(players: QuerySnapshot)
            : List<UserGameDataParameters> {
        val playerList: ArrayList<UserGameDataParameters> = ArrayList()
        for (player in players) {
            playerList.add(player.toObject(UserGameDataParameters::class.java))
        }
        return playerList
    }

    private fun java.lang.Exception.getGamesError(error: ErrorMessage): UseCaseResponse.Failure {
        return UseCaseResponse.Failure(error)
    }
}