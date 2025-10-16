package com.roque.epicmedalsapp.core.di

import com.roque.data.repository.MedalRepositoryImpl
import com.roque.data.repository.MissionsRepositoryImpl
import com.roque.data.repository.StreakRepositoryImpl
import com.roque.domain.repository.MedalRepository
import com.roque.domain.repository.MissionsRepository
import com.roque.domain.repository.StreakRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class InterfacesModule {

    @Singleton
    @Binds
    abstract fun bindMedalRepository(medalRepositoryImpl: MedalRepositoryImpl): MedalRepository

    @Singleton
    @Binds
    abstract fun bindStreakRepository(streakRepositoryImpl: StreakRepositoryImpl): StreakRepository

    @Singleton
    @Binds
    abstract fun bindMissionRepository(missionRepositoryImpl: MissionsRepositoryImpl): MissionsRepository
}