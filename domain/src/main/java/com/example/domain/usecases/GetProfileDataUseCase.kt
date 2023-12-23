package com.example.domain.usecases

import com.example.domain.repositories.UserRepository

class GetProfileDataUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(userId: String) =
        userRepository.getProfileData(userId)
}