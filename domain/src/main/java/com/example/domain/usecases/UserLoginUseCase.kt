package com.example.domain.usecases

import com.example.domain.repositories.AuthenticationRepository

class UserLoginUseCase(private var authenticationRepository: AuthenticationRepository) {
    suspend operator fun invoke(email: String, password: String) =
        authenticationRepository.loginUser(email, password)
}