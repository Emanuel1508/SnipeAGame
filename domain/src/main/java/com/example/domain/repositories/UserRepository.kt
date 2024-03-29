package com.example.domain.repositories

import com.example.domain.models.UserDataParameters
import com.example.domain.models.UserGameDataParameters
import com.example.domain.models.UserGroupUpdateParameters
import com.example.domain.utils.UseCaseResponse

interface UserRepository {
    suspend fun createUser(
        userParameters: UserDataParameters
    ): UseCaseResponse<String>

    suspend fun updateUser(
        userParameters: UserGroupUpdateParameters
    ): UseCaseResponse<String>

    suspend fun getFaction(userId: String): UseCaseResponse<String?>

    suspend fun getProfileData(userId: String): UseCaseResponse<UserDataParameters>

    suspend fun getProfileDataForGames(userId: String): UseCaseResponse<UserGameDataParameters>

    suspend fun updateStats(takedowns: Int, userId: String): UseCaseResponse<String>
}