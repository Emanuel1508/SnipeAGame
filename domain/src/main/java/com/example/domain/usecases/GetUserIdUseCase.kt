package com.example.domain.usecases

import com.example.domain.repositories.AuthenticationRepository

class GetUserIdUseCase(private var authenticationRepository: AuthenticationRepository) {
    operator fun invoke() =
        authenticationRepository.getIdUserLoggedIn()
}