package com.example.domain.usecases

import com.example.domain.models.UserGroupUpdateParameters
import com.example.domain.repositories.UserRepository
import com.example.domain.utils.UseCaseResponse

class UpdateUserGroupsUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(
        userId: String,
        faction: String,
        roles: ArrayList<String>
    ): UseCaseResponse<String> {
        val userDataParameters = UserGroupUpdateParameters(
            userId = userId,
            faction = faction,
            roles = roles
        )
        return userRepository.updateUser(userDataParameters)
    }
}