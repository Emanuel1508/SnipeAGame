package com.example.data.modules

import com.example.data.repository.AchievementsRepositoryImpl
import com.example.data.repository.AuthenticationRepositoryImpl
import com.example.data.repository.UserRepositoryImpl
import com.example.domain.repositories.AchievementsRepository
import com.example.domain.repositories.AuthenticationRepository
import com.example.domain.repositories.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindAuthenticationRepository(authenticationRepositoryImpl: AuthenticationRepositoryImpl): AuthenticationRepository

    @Binds
    abstract fun bindUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository

    @Binds
    abstract fun bindAchievementsRepository(achievementsRepositoryImpl: AchievementsRepositoryImpl): AchievementsRepository
}