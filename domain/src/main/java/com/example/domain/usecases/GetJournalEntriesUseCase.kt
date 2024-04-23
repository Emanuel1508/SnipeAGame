package com.example.domain.usecases

import com.example.domain.repositories.JournalRepository

class GetJournalEntriesUseCase(private val journalRepository: JournalRepository) {
    suspend operator fun invoke(userId: String) =
        journalRepository.getJournalEntries(userId)
}