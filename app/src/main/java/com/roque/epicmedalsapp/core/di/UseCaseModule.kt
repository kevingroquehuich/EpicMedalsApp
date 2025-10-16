package com.roque.epicmedalsapp.core.di

import com.roque.domain.repository.MedalRepository
import com.roque.domain.usecase.GetMedalsFlowUseCase
import com.roque.domain.usecase.ResetAllMedalsUseCase
import com.roque.domain.usecase.SaveMedalsUseCase
import com.roque.epicmedalsapp.domain.usecase.UpdateMedalsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
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

    @Provides
    @Singleton
    fun provideUpdateMedalUseCase() = UpdateMedalsUseCase()
}