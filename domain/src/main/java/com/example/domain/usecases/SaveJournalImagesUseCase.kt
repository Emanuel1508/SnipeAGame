package com.example.domain.usecases

import android.net.Uri
import com.example.domain.repositories.JournalRepository

class SaveJournalImagesUseCase(private val journalRepository: JournalRepository) {
    suspend operator fun invoke(imageUris: MutableList<Uri>, userId: String, journalId: String) =
        journalRepository.saveJournalImages(imageUris, userId, journalId)
}