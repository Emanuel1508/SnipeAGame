package com.example.data.repository

import com.example.data.utils.DatabaseConstants
import com.example.data.utils.StringConstants
import com.example.domain.models.JournalParameters
import com.example.domain.repositories.JournalRepository
import com.example.domain.utils.ErrorMessage
import com.example.domain.utils.UseCaseResponse
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await
import java.net.UnknownHostException
import javax.inject.Inject

class JournalRepositoryImpl @Inject constructor(private val firestore: FirebaseFirestore) :
    JournalRepository {
    private val TAG = this::class.java.simpleName

    override suspend fun saveGameToJournal(journalData: JournalParameters): UseCaseResponse<String> {
        return try {
            firestore.collection(DatabaseConstants.PROFILES)
                .document(journalData.userId)
                .collection(DatabaseConstants.JOURNAL)
                .document(journalData.gameId)
                .set(journalData)
            UseCaseResponse.Success(StringConstants.SUCCESS)
        } catch (unknownHostException: UnknownHostException) {
            unknownHostException.getJournalError(ErrorMessage.NO_NETWORK)
        } catch (exception: Exception) {
            exception.getJournalError(ErrorMessage.GENERAL)
        }
    }

    override suspend fun getJournalEntries(userId: String): UseCaseResponse<List<JournalParameters>> {
        return try {
            val documentSnapshot = firestore.collection(DatabaseConstants.PROFILES)
                .document(userId)
                .collection(DatabaseConstants.JOURNAL)
                .get()
                .await()
            val journalEntries = documentToJournalObject(documentSnapshot)
            UseCaseResponse.Success(journalEntries)
        } catch (unknownHostException: UnknownHostException) {
            unknownHostException.getJournalError(ErrorMessage.NO_NETWORK)
        } catch (exception: Exception) {
            exception.getJournalError(ErrorMessage.GENERAL)
        }
    }

    override suspend fun getJournalDetails(
        userId: String,
        journalId: String
    ): UseCaseResponse<JournalParameters> {
        return try {
            val documentSnapshot = firestore.collection(DatabaseConstants.PROFILES)
                .document(userId)
                .collection(DatabaseConstants.JOURNAL)
                .document(journalId)
                .get()
                .await()
            val journalDetailsData: JournalParameters? =
                documentSnapshot.toObject(JournalParameters::class.java)
            if (journalDetailsData != null)
                UseCaseResponse.Success(journalDetailsData)
            else
                UseCaseResponse.Failure(ErrorMessage.GENERAL)
        } catch (unknownHostException: UnknownHostException) {
            unknownHostException.getJournalError(ErrorMessage.NO_NETWORK)
        } catch (exception: Exception) {
            exception.getJournalError(ErrorMessage.GENERAL)
        }
    }

    private fun documentToJournalObject(documents: QuerySnapshot): List<JournalParameters> {
        val list: ArrayList<JournalParameters> = ArrayList()
        for (document in documents) {
            list.add(document.toObject(JournalParameters::class.java))
        }
        return list
    }

    private fun java.lang.Exception.getJournalError(error: ErrorMessage): UseCaseResponse.Failure {
        return UseCaseResponse.Failure(error)
    }
}