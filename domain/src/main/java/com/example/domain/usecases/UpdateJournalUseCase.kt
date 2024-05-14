package com.example.domain.usecases

import com.example.domain.models.JournalUpdateParameters
import com.example.domain.repositories.JournalRepository
import com.example.domain.utils.UseCaseResponse

class UpdateJournalUseCase(private val journalRepository: JournalRepository) {
    suspend operator fun invoke(
        userId: String,
        journalId: String,
        rating: String,
        journalText: String
    ): UseCaseResponse<String> {
        val journalUpdateParameters = JournalUpdateParameters(
            userId = userId,
            journalId = journalId,
            rating = rating,
            journalText = journalText
        )
        return journalRepository.updateJournalDetails(journalUpdateParameters)
    }
}