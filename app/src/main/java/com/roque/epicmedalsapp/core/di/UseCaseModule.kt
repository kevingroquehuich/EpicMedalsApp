package com.roque.epicmedalsapp.core.di

import com.roque.domain.repository.MedalRepository
import com.roque.domain.usecase.GetMedalsFlowUseCase
import com.roque.domain.usecase.ResetAllMedalsUseCase
import com.roque.domain.usecase.SaveMedalsUseCase
import com.roque.domain.usecase.UpdateMedalsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

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
}