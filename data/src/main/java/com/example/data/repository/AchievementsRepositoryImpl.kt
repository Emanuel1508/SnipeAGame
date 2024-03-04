package com.example.data.repository

import android.util.Log
import com.example.data.utils.DatabaseConstants
import com.example.data.utils.StringConstants
import com.example.domain.models.AchievementsParameters
import com.example.domain.repositories.AchievementsRepository
import com.example.domain.utils.ErrorMessage
import com.example.domain.utils.UseCaseResponse
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await
import java.net.UnknownHostException
import javax.inject.Inject

class AchievementsRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : AchievementsRepository {
    private var achievementsList: List<AchievementsParameters> = ArrayList()
    private var unlockedAchievements: List<AchievementsParameters> = ArrayList()

    private val TAG = this::class.simpleName
    override suspend fun getAvailableAchievements(userId: String): UseCaseResponse<List<AchievementsParameters>> {
        return try {
            val availableAchievementsDocuments =
                firestore.collection(DatabaseConstants.ACHIEVEMENTS)
                    .get()
                    .await()
            val unlockedAchievementsDocuments = firestore.collection(DatabaseConstants.PROFILES)
                .document(userId)
                .collection(DatabaseConstants.MY_ACHIEVEMENTS)
                .get()
                .await()

            unlockedAchievements = documentToAchievementObject(unlockedAchievementsDocuments)
            achievementsList = documentToAchievementObject(availableAchievementsDocuments)
            val lockedAchievements =
                retainLockedAchievements(achievementsList, unlockedAchievements)

            UseCaseResponse.Success(lockedAchievements)
        } catch (unknownHostException: UnknownHostException) {
            unknownHostException.getAchievementError(ErrorMessage.NO_NETWORK)
        } catch (exception: Exception) {
            exception.getAchievementError(ErrorMessage.GENERAL)
        }
    }

    override suspend fun unlockAchievement(
        userId: String,
        achievement: AchievementsParameters
    ): UseCaseResponse<String> {
        return try {
            firestore.collection(DatabaseConstants.PROFILES)
                .document(userId)
                .collection(DatabaseConstants.MY_ACHIEVEMENTS)
                .document()
                .set(achievement)
            UseCaseResponse.Success(StringConstants.SUCCESS)
        } catch (unknownHostException: UnknownHostException) {
            unknownHostException.getAchievementError(ErrorMessage.NO_NETWORK)
        } catch (exception: Exception) {
            exception.getAchievementError(ErrorMessage.GENERAL)
        }
    }

    override suspend fun getUnlockedAchievements(userId: String): UseCaseResponse<List<AchievementsParameters>> {
        return try {
            val documents = firestore.collection(DatabaseConstants.PROFILES)
                .document(userId)
                .collection(DatabaseConstants.MY_ACHIEVEMENTS)
                .get()
                .await()
            unlockedAchievements = documentToAchievementObject(documents)
            for (element in unlockedAchievements) {
                Log.v(TAG, element.title)
            }
            UseCaseResponse.Success(unlockedAchievements)
        } catch (unknownHostException: UnknownHostException) {
            unknownHostException.getAchievementError(ErrorMessage.NO_NETWORK)
        } catch (exception: Exception) {
            exception.getAchievementError(ErrorMessage.GENERAL)
        }
    }

    private fun documentToAchievementObject(documents: QuerySnapshot): List<AchievementsParameters> {
        val list: ArrayList<AchievementsParameters> = ArrayList()
        for (document in documents) {
            list.add(document.toObject(AchievementsParameters::class.java))
        }
        return list
    }

    private fun retainLockedAchievements(
        availableAchievements: List<AchievementsParameters>,
        unlockedAchievements: List<AchievementsParameters>
    ) = availableAchievements.filterNot { it in unlockedAchievements }

    private fun java.lang.Exception.getAchievementError(error: ErrorMessage): UseCaseResponse.Failure {
        return UseCaseResponse.Failure(error)
    }
}

