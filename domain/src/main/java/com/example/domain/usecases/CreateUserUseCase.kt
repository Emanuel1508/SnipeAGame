package com.example.domain.usecases

import com.example.domain.models.UserDataParameters
import com.example.domain.repositories.UserRepository
import com.example.domain.utils.UseCaseResponse

class CreateUserUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(
        userId: String,
        firstName: String? = null,
        lastName: String? = null,
        email: String? = null,
        phone: String? = null,
        faction: String? = null,
        roles: ArrayList<String>? = null
    ): UseCaseResponse<String> {
        val name = if (firstName != null && lastName != null) "$firstName $lastName" else null
        val userDataParameters = UserDataParameters(
            userId = userId,
            name = name,
            email = email,
            phone = phone,
            faction = faction,
            roles = roles,
            takedowns = 0,
            games = 0
        )
        return userRepository.createUser(userDataParameters)
    }
}