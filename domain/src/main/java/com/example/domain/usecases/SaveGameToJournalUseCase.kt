package com.example.domain.usecases

import com.example.domain.models.JournalParameters
import com.example.domain.repositories.JournalRepository
import com.example.domain.utils.UseCaseResponse

class SaveGameToJournalUseCase(private val journalRepository: JournalRepository) {
    suspend operator fun invoke(
        gameId: String,
        userId: String,
        location: String,
        date: String,
        time: String,
        numberOfPlayers: String,
        takedowns: String,
        rating: String,
        journalText: String
    ): UseCaseResponse<String> {
        val journalParameters = JournalParameters(
            gameId = gameId,
            userId = userId,
            location = location,
            date = date,
            time = time,
            numberOfPlayers = numberOfPlayers,
            takedowns = takedowns,
            rating = rating,
            journalText = journalText
        )
        return journalRepository.saveGameToJournal(journalParameters)
    }
}