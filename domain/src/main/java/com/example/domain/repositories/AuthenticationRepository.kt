package com.example.domain.repositories

import com.example.domain.utils.UseCaseResponse

interface AuthenticationRepository {
    suspend fun registerUser(
        email: String,
        password: String
    ): UseCaseResponse<String>

    suspend fun loginUser(
        email: String,
        password: String
    ): UseCaseResponse<Unit>

    fun getIdUserLoggedIn(): UseCaseResponse<String>
}