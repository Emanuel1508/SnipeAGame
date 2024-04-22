package com.example.data.repository

import com.example.data.utils.DatabaseConstants
import com.example.data.utils.StringConstants
import com.example.domain.models.JournalParameters
import com.example.domain.repositories.JournalRepository
import com.example.domain.utils.ErrorMessage
import com.example.domain.utils.UseCaseResponse
import com.google.firebase.firestore.FirebaseFirestore
import java.net.UnknownHostException
import javax.inject.Inject

class JournalRepositoryImpl @Inject constructor(private val firestore: FirebaseFirestore) :
    JournalRepository {
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

    private fun java.lang.Exception.getJournalError(error: ErrorMessage): UseCaseResponse.Failure {
        return UseCaseResponse.Failure(error)
    }
}