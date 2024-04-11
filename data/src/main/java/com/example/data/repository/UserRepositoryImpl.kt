package com.example.data.repository

import com.example.data.utils.DatabaseConstants
import com.example.data.utils.StringConstants
import com.example.domain.models.UserDataParameters
import com.example.domain.models.UserGameDataParameters
import com.example.domain.models.UserGroupUpdateParameters
import com.example.domain.models.UserStats
import com.example.domain.repositories.UserRepository
import com.example.domain.utils.ErrorMessage
import com.example.domain.utils.UseCaseResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.net.UnknownHostException
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) : UserRepository {
    override suspend fun createUser(
        userParameters: UserDataParameters
    ): UseCaseResponse<String> {
        return try {
            firestore.collection(DatabaseConstants.PROFILES)
                .document(userParameters.userId)
                .set(userParameters)

            UseCaseResponse.Success(StringConstants.SUCCESS)
        } catch (unknownHostException: UnknownHostException) {
            unknownHostException.getUserError(ErrorMessage.NO_NETWORK)
        } catch (exception: Exception) {
            exception.getUserError(ErrorMessage.GENERAL)
        }
    }

    override suspend fun updateUser(
        userParameters: UserGroupUpdateParameters
    ): UseCaseResponse<String> {
        return try {
            firestore.collection(DatabaseConstants.PROFILES)
                .document(userParameters.userId)
                .update(DatabaseConstants.FACTION, userParameters.faction)

            firestore.collection(DatabaseConstants.PROFILES)
                .document(userParameters.userId)
                .update(DatabaseConstants.ROLES, userParameters.roles)

            UseCaseResponse.Success(StringConstants.SUCCESS)
        } catch (unknownHostException: UnknownHostException) {
            unknownHostException.getUserError(ErrorMessage.NO_NETWORK)
        } catch (exception: Exception) {
            exception.getUserError(ErrorMessage.GENERAL)
        }
    }

    override suspend fun getFaction(userId: String):
            UseCaseResponse<String?> {
        return try {
            val result = firestore.collection(DatabaseConstants.PROFILES)
                .document(userId)
                .get()
                .await()
            UseCaseResponse.Success(result.getString(DatabaseConstants.FACTION))
        } catch (unknownHostException: UnknownHostException) {
            unknownHostException.getUserError(ErrorMessage.NO_NETWORK)
        } catch (exception: Exception) {
            exception.getUserError(ErrorMessage.GENERAL)
        }
    }

    override suspend fun getProfileData(userId: String):
            UseCaseResponse<UserDataParameters> {
        return try {
            val result = firestore.collection(DatabaseConstants.PROFILES)
                .document(userId)
                .get()
                .await()
            val finalResult: UserDataParameters? = result.toObject(UserDataParameters::class.java)
            if (finalResult != null) {
                UseCaseResponse.Success(finalResult)
            } else {
                UseCaseResponse.Failure(ErrorMessage.GENERAL)
            }
        } catch (unknownHostException: UnknownHostException) {
            unknownHostException.getUserError(ErrorMessage.NO_NETWORK)
        } catch (exception: Exception) {
            exception.getUserError(ErrorMessage.GENERAL)
        }
    }

    override suspend fun getProfileDataForGames(userId: String):
            UseCaseResponse<UserGameDataParameters> {
        return try {
            val result = firestore.collection(DatabaseConstants.PROFILES)
                .document(userId)
                .get()
                .await()
            val finalResult = documentToUserGameData(result)
            UseCaseResponse.Success(finalResult)
        } catch (unknownHostException: UnknownHostException) {
            unknownHostException.getUserError(ErrorMessage.NO_NETWORK)
        } catch (exception: Exception) {
            exception.getUserError(ErrorMessage.GENERAL)
        }
    }

    override suspend fun updateStats(takedowns: Int, userId: String): UseCaseResponse<String> {
        return try {
            getPlayerStats(takedowns, userId)
            UseCaseResponse.Success(StringConstants.SUCCESS)
        } catch (unknownHostException: UnknownHostException) {
            unknownHostException.getUserError(ErrorMessage.NO_NETWORK)
        } catch (exception: Exception) {
            exception.getUserError(ErrorMessage.GENERAL)
        }
    }

    override suspend fun forgotPassword(email: String): UseCaseResponse<String> {
        return try {
            firebaseAuth.sendPasswordResetEmail(email)
            UseCaseResponse.Success(StringConstants.SUCCESS)
        } catch (unknownHostException: UnknownHostException) {
            unknownHostException.getUserError(ErrorMessage.NO_NETWORK)
        } catch (exception: Exception) {
            exception.getUserError(ErrorMessage.GENERAL)
        }
    }

    override suspend fun getUserStats(userId: String): UseCaseResponse<UserStats> {
        return try {
            val userSnapshot = firestore.collection(DatabaseConstants.PROFILES)
                .document(userId)
                .get()
                .await()
            val userStats = documentToUserStats(userSnapshot)
            UseCaseResponse.Success(userStats)
        } catch (unknownHostException: UnknownHostException) {
            unknownHostException.getUserError(ErrorMessage.NO_NETWORK)
        } catch (exception: Exception) {
            exception.getUserError(ErrorMessage.GENERAL)
        }
    }

    private fun documentToUserGameData(document: DocumentSnapshot): UserGameDataParameters {
        val profileData = document.toObject(UserGameDataParameters::class.java)
        if (profileData != null) {
            return profileData
        }
        return UserGameDataParameters()
    }

    private fun documentToUserStats(document: DocumentSnapshot): UserStats {
        val statsData = document.toObject(UserStats::class.java)
        if(statsData != null) {
            return statsData
        }
        return UserStats()
    }

    private suspend fun getPlayerStats(takedowns: Int, userId: String) {
        val playerDocumentSnapshot = firestore.collection(DatabaseConstants.PROFILES)
            .document(userId)
            .get()
            .await()
        var playerTakedowns = playerDocumentSnapshot.getLong(DatabaseConstants.TAKEDOWNS)
        var playerGamesCount = playerDocumentSnapshot.getLong(DatabaseConstants.GAME_COUNT)
        playerGamesCount?.let {
            playerGamesCount++
        }
        playerTakedowns?.let {
            playerTakedowns += takedowns
        }
        if (playerGamesCount != null && playerTakedowns != null) {
            updatePlayerStats(userId, playerGamesCount.toInt(), playerTakedowns.toInt())
        }
    }

    private fun updatePlayerStats(userId: String, gameCount: Int, takedowns: Int) {
        firestore.collection(DatabaseConstants.PROFILES)
            .document(userId)
            .update(
                mapOf(
                    DatabaseConstants.TAKEDOWNS to takedowns,
                    DatabaseConstants.GAME_COUNT to gameCount
                )
            )
    }

    private fun java.lang.Exception.getUserError(error: ErrorMessage): UseCaseResponse.Failure {
        return UseCaseResponse.Failure(error)
    }
}