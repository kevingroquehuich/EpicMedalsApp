package com.roque.epicmedalsapp.di

import com.roque.domain.repository.MedalRepository
import com.roque.domain.usecase.GetMedalsFlowUseCase
import com.roque.domain.usecase.ResetAllMedalsUseCase
import com.roque.domain.usecase.SaveMedalsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideGetMedalsFlowUseCase(repository: MedalRepository) =
        GetMedalsFlowUseCase(repository)

    @Provides
    @Singleton
    fun provideSaveMedalsUseCase(repository: MedalRepository) =
        SaveMedalsUseCase(repository)

    @Provides
    @Singleton
    fun provideResetAllMedalsUseCase(repository: MedalRepository) =
        ResetAllMedalsUseCase(repository)
}