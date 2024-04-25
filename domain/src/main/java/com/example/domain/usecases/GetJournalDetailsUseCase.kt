package com.example.domain.usecases

import com.example.domain.repositories.JournalRepository

class GetJournalDetailsUseCase(private val journalRepository: JournalRepository) {
    suspend operator fun invoke(userId: String, journalId: String) =
        journalRepository.getJournalDetails(userId, journalId)
}