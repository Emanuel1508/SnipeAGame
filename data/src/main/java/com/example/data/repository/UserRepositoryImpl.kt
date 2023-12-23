package com.example.data.repository

import com.example.data.utils.DatabaseConstants
import com.example.data.utils.StringConstants
import com.example.domain.models.UserDataParameters
import com.example.domain.models.UserGroupUpdateParameters
import com.example.domain.repositories.UserRepository
import com.example.domain.utils.ErrorMessage
import com.example.domain.utils.UseCaseResponse
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.net.UnknownHostException
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
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

    private fun java.lang.Exception.getUserError(error: ErrorMessage): UseCaseResponse.Failure {
        return UseCaseResponse.Failure(error)
    }
}