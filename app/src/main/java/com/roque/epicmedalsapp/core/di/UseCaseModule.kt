package com.roque.epicmedalsapp.core.di

import com.roque.domain.repository.MedalRepository
import com.roque.domain.repository.MissionsRepository
import com.roque.domain.repository.StreakRepository
import com.roque.domain.usecase.medal.GetMedalsFlowUseCase
import com.roque.domain.usecase.streak.GetStreakFlowUseCase
import com.roque.domain.usecase.streak.RecordDailyActivityUseCase
import com.roque.domain.usecase.medal.ResetAllMedalsUseCase
import com.roque.domain.usecase.medal.SaveMedalsUseCase
import com.roque.domain.usecase.medal.UpdateMedalsUseCase
import com.roque.domain.usecase.missions.GetMissionsUseCase
import com.roque.domain.usecase.streak.UpdateStreakMedalsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    /** MEDALS **/
    @Provides
    fun provideGetMedalsFlowUseCase(repository: MedalRepository) =
        GetMedalsFlowUseCase(repository)

    @Provides
    fun provideSaveMedalsUseCase(repository: MedalRepository) =
        SaveMedalsUseCase(repository)

    @Provides
    fun provideResetAllMedalsUseCase(repository: MedalRepository) =
        ResetAllMedalsUseCase(repository)

    @Provides
    fun provideUpdateMedalUseCase() = UpdateMedalsUseCase()


    /** STREAK **/
    @Provides
    fun provideGetStreakFlowUseCase(repository: StreakRepository) =
        GetStreakFlowUseCase(repository)

    @Provides
    fun provideRecordDailyActivityUseCase(repository: StreakRepository) =
        RecordDailyActivityUseCase(repository)

    @Provides
    fun provideUpdateStreakMedalsUseCase(
        medalRepository: MedalRepository,
        streakRepository: StreakRepository
    ) = UpdateStreakMedalsUseCase(medalRepository, streakRepository)

    /** MISSIONS **/

    @Provides
    fun provideGetMissionsUseCase(repository: MissionsRepository) =
        GetMissionsUseCase(repository)
}