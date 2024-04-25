package com.example.domain.repositories

import com.example.domain.models.JournalParameters
import com.example.domain.utils.UseCaseResponse

interface JournalRepository {
    suspend fun saveGameToJournal(journalData: JournalParameters): UseCaseResponse<String>

    suspend fun getJournalEntries(userId: String): UseCaseResponse<List<JournalParameters>>

    suspend fun getJournalDetails(userId: String, journalId: String): UseCaseResponse<JournalParameters>
}