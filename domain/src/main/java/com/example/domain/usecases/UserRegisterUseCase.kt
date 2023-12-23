package com.example.domain.usecases

import com.example.domain.repositories.AuthenticationRepository

class UserRegisterUseCase(private val authenticationRepository: AuthenticationRepository) {
    suspend operator fun invoke(email: String, password: String) =
        authenticationRepository.registerUser(email, password)
}