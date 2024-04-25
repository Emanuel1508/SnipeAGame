package com.example.data.modules

import com.example.domain.repositories.AchievementsRepository
import com.example.domain.repositories.AuthenticationRepository
import com.example.domain.repositories.GamesRepository
import com.example.domain.repositories.JournalRepository
import com.example.domain.repositories.UserRepository
import com.example.domain.usecases.CreateGameUseCase
import com.example.domain.usecases.CreateUserUseCase
import com.example.domain.usecases.FinishGameUseCase
import com.example.domain.usecases.ForgotPasswordUseCase
import com.example.domain.usecases.GetAvailableAchievementsUseCase
import com.example.domain.usecases.GetFactionUseCase
import com.example.domain.usecases.GetGamesUseCase
import com.example.domain.usecases.GetJournalDetailsUseCase
import com.example.domain.usecases.GetJournalEntriesUseCase
import com.example.domain.usecases.GetMyGamesUseCase
import com.example.domain.usecases.GetPlayersUseCase
import com.example.domain.usecases.GetProfileDataForGamesUseCase
import com.example.domain.usecases.GetProfileDataUseCase
import com.example.domain.usecases.GetUnlockedAchievementsUseCase
import com.example.domain.usecases.GetUserIdUseCase
import com.example.domain.usecases.GetUserStatsUseCase
import com.example.domain.usecases.JoinGameUseCase
import com.example.domain.usecases.LeaveGameUseCase
import com.example.domain.usecases.SaveGameToJournalUseCase
import com.example.domain.usecases.UnlockAchievementUseCase
import com.example.domain.usecases.UpdateUserGroupsUseCase
import com.example.domain.usecases.UserLoginUseCase
import com.example.domain.usecases.UserRegisterUseCase
import com.example.domain.usecases.UserUpdateStatsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModules {
    @Singleton
    @Provides
    fun provideRegisterUseCase(repository: AuthenticationRepository) =
        UserRegisterUseCase(repository)

    @Singleton
    @Provides
    fun provideLoginUseCase(repository: AuthenticationRepository) =
        UserLoginUseCase(repository)

    @Singleton
    @Provides
    fun provideCreateUseCase(repository: UserRepository) =
        CreateUserUseCase(repository)

    @Singleton
    @Provides
    fun provideGetUserIdUseCase(repository: AuthenticationRepository) =
        GetUserIdUseCase(repository)

    @Singleton
    @Provides
    fun provideGroupUpdateUseCase(repository: UserRepository) =
        UpdateUserGroupsUseCase(repository)

    @Singleton
    @Provides
    fun provideFactionUseCase(repository: UserRepository) =
        GetFactionUseCase(repository)

    @Singleton
    @Provides
    fun provideGetProfileDataUseCase(repository: UserRepository) =
        GetProfileDataUseCase(repository)

    @Singleton
    @Provides
    fun provideGetAvailableAchievementsUseCase(repository: AchievementsRepository) =
        GetAvailableAchievementsUseCase(repository)

    @Singleton
    @Provides
    fun provideUnlockAchievementUseCase(repository: AchievementsRepository) =
        UnlockAchievementUseCase(repository)

    @Singleton
    @Provides
    fun provideGetUnlockedAchievementsUseCase(repository: AchievementsRepository) =
        GetUnlockedAchievementsUseCase(repository)

    @Singleton
    @Provides
    fun provideCreateGameUseCase(repository: GamesRepository) =
        CreateGameUseCase(repository)

    @Singleton
    @Provides
    fun provideGetGamesUseCase(repository: GamesRepository) =
        GetGamesUseCase(repository)

    @Singleton
    @Provides
    fun provideGetProfileDataForGamesUseCase(repository: UserRepository) =
        GetProfileDataForGamesUseCase(repository)

    @Singleton
    @Provides
    fun provideJoinGameUseCase(repository: GamesRepository) =
        JoinGameUseCase(repository)

    @Singleton
    @Provides
    fun provideMyGamesUseCase(repository: GamesRepository) =
        GetMyGamesUseCase(repository)

    @Singleton
    @Provides
    fun provideGetPlayersUseCase(repository: GamesRepository) =
        GetPlayersUseCase(repository)

    @Singleton
    @Provides
    fun provideLeaveGameUseCase(repository: GamesRepository) =
        LeaveGameUseCase(repository)

    @Singleton
    @Provides
    fun provideUserUpdateStatsUseCase(repository: UserRepository) =
        UserUpdateStatsUseCase(repository)

    @Singleton
    @Provides
    fun provideFinishGameUseCase(repository: GamesRepository) =
        FinishGameUseCase(repository)

    @Singleton
    @Provides
    fun provideForgotPasswordUseCase(repository: UserRepository) =
        ForgotPasswordUseCase(repository)

    @Singleton
    @Provides
    fun provideUserStatsUseCase(repository: UserRepository) =
        GetUserStatsUseCase(repository)

    @Singleton
    @Provides
    fun provideSaveGameToJournalUseCase(repository: JournalRepository) =
        SaveGameToJournalUseCase(repository)

    @Singleton
    @Provides
    fun provideGetJournalEntriesUseCase(repository: JournalRepository) =
        GetJournalEntriesUseCase(repository)

    @Singleton
    @Provides
    fun provideGetJournalDetailsUseCase(repository: JournalRepository) =
        GetJournalDetailsUseCase(repository)
}