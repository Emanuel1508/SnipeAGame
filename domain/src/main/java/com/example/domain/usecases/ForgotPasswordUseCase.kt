package com.example.domain.usecases

import com.example.domain.repositories.UserRepository

class ForgotPasswordUseCase(private var repository: UserRepository) {
    suspend operator fun invoke(email: String) = repository.forgotPassword(email)
}