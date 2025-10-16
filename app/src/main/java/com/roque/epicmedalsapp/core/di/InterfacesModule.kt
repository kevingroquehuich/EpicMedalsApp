package com.roque.epicmedalsapp.core.di

import com.roque.data.repository.MedalRepositoryImpl
import com.roque.domain.repository.MedalRepository
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
}